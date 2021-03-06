/*
 * Copyright (c) 2016, Anthony Anderson
 * Copyright (c) 2016, Minecraft Forge
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 *  Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package us.illyohs.sparkyforge.util;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHIssueComment;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHLabel;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHPullRequestCommitDetail;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;

import static org.kohsuke.github.GHCommitState.*;


public class GitHubHelper
{
    String repo;
    GitHub gitHub;

    public GitHubHelper(String token, String repo)
    {
        this.repo = repo;
        try
        {
            this.gitHub = GitHub.connectUsingOAuth(token);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public GHRepository getRepo() throws IOException
    {
        return gitHub.getRepository(repo);
    }

    public String getDefaultBranch() throws IOException
    {
        return getRepo().getDefaultBranch();
    }

    public GHPullRequest getPullRequest(int id) throws IOException
    {
        return getRepo().getPullRequest(id);
    }

    public String getPullRequestTitle(int id) throws IOException
    {
        return getPullRequest(id).getTitle();
    }

    public GHUser getPulAuthor(int id) throws IOException
    {
        return getPullRequest(id).getUser();
    }

    public GHIssueState isOpen(int id) throws IOException
    {
        return getPullRequest(id).getState();
    }

    public boolean isMerged(int id) throws IOException
    {
        return getPullRequest(id).isMerged();
    }

    public URL getPullRequestURL(int id) throws IOException
    {
        return getPullRequest(id).getHtmlUrl();
    }

    public boolean isPointedToDefualt(int id) throws IOException
    {
        return Objects.equals(getDefaultBranch(), getPullRequest(id).getBase().getRef());
    }

    public boolean haveIMadeAComment(int id) throws IOException
    {
        for(GHIssueComment i: getPullRequest(id).getComments()) {
            if (i.getUser() == gitHub.getMyself()) {
                return true;
            }
        }
        return false;
    }

    public void setStatus(int id, boolean isGood) throws IOException
    {
        GHPullRequest pr = getPullRequest(id);
        for (GHPullRequestCommitDetail i: pr.listCommits().asList())
        {
            String sha1 = i.getSha();
            if (!isGood) {
                getRepo().createCommitStatus(sha1, FAILURE, "", "Is not pointed to the default branch");
            }
            else {
                getRepo().createCommitStatus(sha1, SUCCESS, "", "Is pointed to the default branch");
            }
        }
    }


    public GHIssue getIssue(int id) throws IOException
    {
        return getRepo().getIssue(id);
    }

    public Collection<GHLabel> getLabels(int id) throws IOException
    {
        return getIssue(id).getLabels();
    }

    public boolean doesLabelExist(String label) throws IOException
    {
        List<String> labList = new ArrayList<>();
        this.getRepo().listLabels().forEach(l -> labList.add(l.getName()));

        return labList.contains(label);
    }

    public GHLabel getLabelFromName(String name) throws IOException
    {
        return getRepo().getLabel(name);
    }


    public void removeLabel(int id, String label) throws IOException
    {
        for (GHLabel lbs: getLabels(id))
        {
            if (lbs.getName() == label) {
                getLabels(id).remove(lbs);
            }
        }

    }

    public boolean isIssueClosed(int id) throws IOException
    {
        GHIssueState state = getIssue(id).getState();
        if (state == GHIssueState.CLOSED)
        {
            return true;
        } else
        {
            return false;
        }
    }

    public void closeIssue(int id) throws IOException
    {
        getIssue(id).close();
    }

    public void reopenIssue(int id) throws IOException
    {
        getIssue(id).reopen();
    }

}
