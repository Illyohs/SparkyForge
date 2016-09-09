/*
 *     Copyright (C) 2016  Anthony Anderson
 *
 *     This library is free software; you can redistribute it and/or
 *     modify it under the terms of the GNU Library General Public
 *     License as published by the Free Software Foundation; either
 *     version 2 of the License, or (at your option) any later version.
 *
 *     This library is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *     Library General Public License for more details.
 *
 *     You should have received a copy of the GNU Library General Public
 *     License along with this library; if not, write to the Free Software
 *     Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package us.illyohs.sparkyforge.hooker.hooks.github;

import java.io.IOException;
import java.net.URL;

import us.illyohs.sparkyforge.SparkyForge;
import us.illyohs.sparkyforge.util.MessageUtils;
import us.illyohs.sparkyforge.util.Shorteners;
import us.illyohs.sparkyforge.util.WebUtils;

import com.google.gson.JsonObject;

import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHUser;

public class GitHubDispatcher
{

    public GitHubDispatcher(String json)
    {
        JsonObject jObj   = WebUtils.readJsonObjectFromString(json);

        String action     = jObj.get("action").getAsString();
        int    id         = jObj.get("number").getAsInt();
        eventMatcher(action, id);
    }

    public void eventMatcher(String action, int id)
    {
        switch (action)
        {
            case "opened": handleOpen(id);
                break;
            case "reopened": handleReOpend(id);
                break;
            case "closed": handleClosed(id);
                break;
            case "synchronize": handleSync(id);
                break;
            default:
                break;
        }
    }

    private void handleOpen(int id)
    {
        try {
            GHPullRequest pr           = SparkyForge.getGitbot().getPullRequest(id);
            String defaultBranch       = SparkyForge.getGitbot().getDefaultBranch();
            boolean isPointedToDefault = SparkyForge.getGitbot().isPointedToDefualt(id);
            GHUser user = pr.getUser();
            URL url = pr.getHtmlUrl();

            String gitComment = "@" + user.getLogin() + " Your pull request is not pointed to the default branch __" +
                    defaultBranch + "__. Please retarget to __" + defaultBranch + "__. if you want this to be mered!";

            String ircNonMerge = "[GitHub] "+ user.getName() + "(" + user.getLogin() + ") has just open pull request: "+
                    pr.getTitle() +", that is NOT pointed to the current default branch " + defaultBranch +
                    " and WON'T BE merged! " + Shorteners.gitIo(url);

            String ircCanMerge ="[GitHub] "+ user.getName() + "(" + user.getLogin() + ") has just open pull request: "
                    + pr.getTitle() + ", that is  pointed to the current default branch (" + defaultBranch +
                    ") and CAN BE merged! " + Shorteners.gitIo(url);


            if (!isPointedToDefault)
            {
                SparkyForge.getGitbot().setStatus(id, false);
                MessageUtils.sendMessageToChannel(ircNonMerge);
                MessageUtils.sendIssueMessage(id, gitComment);

            } else
            {
                SparkyForge.getGitbot().setStatus(id, true);
                MessageUtils.sendLexHandledMessageToChannel(ircCanMerge);
            }
            SparkyForge.getGitbot().setStatus(id, isPointedToDefault);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void handleReOpend(int id)
    {
        try
        {
            GHPullRequest pr    = SparkyForge.getGitbot().getPullRequest(id);
            String        title = pr.getTitle();
            URL           url   = pr.getHtmlUrl();

            String message = "[GitHub] Pull Request: (" + title + "), has been reopened! " + Shorteners.gitIo(url);
            MessageUtils.sendLexHandledMessageToChannel(message);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void handleSync(int id)
    {

        try {

            boolean isPointedToDefault = SparkyForge.getGitbot().isPointedToDefualt(id);

            SparkyForge.getGitbot().setStatus(id, isPointedToDefault);

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void handleClosed(int id)
    {
        try {
            GHPullRequest pr    = SparkyForge.getGitbot().getPullRequest(id);
            String        title = pr.getTitle();
            URL           url   = pr.getHtmlUrl();
            MessageUtils.sendLexHandledMessageToChannel("[GitHub] Pull Request: (" + title + "), has been Closed! " + Shorteners.gitIo(url));

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
