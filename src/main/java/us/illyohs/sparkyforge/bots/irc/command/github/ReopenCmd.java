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

package us.illyohs.sparkyforge.bots.irc.command.github;

import java.io.IOException;

import us.illyohs.sparkyforge.SparkyForge;
import us.illyohs.sparkyforge.bots.irc.command.BaseCMD;
import us.illyohs.sparkyforge.util.MessageUtils;

import org.kitteh.irc.client.library.element.Channel;
import org.kitteh.irc.client.library.element.User;

public class ReopenCmd extends BaseCMD
{
    public ReopenCmd()
    {
        super("reopen");
    }

    @Override
    public String getHelp()
    {
        return "Usage: reopen <id>";
    }

    @Override
    public boolean execute(Channel channel, User user, String... args)
    {
        int id = string2int(args[1]); // Integer.parseInt(args[1]);
        try
        {
            if (isUserListed(user))
            {
                boolean isClosed = SparkyForge.getGitbot().isIssueClosed(id);
                if (isClosed)
                {
                    MessageUtils.sendIssueMessage(id, "Reopened by: @" + getGitHubUserName(user));
                    SparkyForge.getGitbot().reopenIssue(id);
                } else
                {
                    channel.sendMessage(user.getNick() + ", Issue is already open");
                }
            } else {
                channel.sendMessage(user.getNick() + ", You do not have permission to use this command!");
            }
        } catch (IOException e)
        {
            channel.sendMessage(user.getNick() + ": Error something something something");
        } catch (NumberFormatException e)
        {

        }
        return true;
    }
}
