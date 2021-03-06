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

import us.illyohs.sparkyforge.SparkyForge;

import org.kitteh.irc.client.library.element.User;

public class MessageUtils
{
    /**
     * We don't want to ping lex
     * @param message
     * @return
     */
    private static String lexHelper(String message)
    {
        if (message.contains("LexManos")) {
            return message.replace("LexManos", "Lex");
        } else {
            return message;
        }
    }

    /**
     *
     * @param message
     */
    public static void sendLexHandledMessageToChannel(String message)
    {
        sendMessageToChannel(lexHelper(message));
    }

    public static void sendMessageToChannel(String message)
    {
        SparkyForge.getIrcbot().getConChannel().sendMessage(message);
    }

    /**
     *
     * @param user
     * @param message
     */
    public static void sendMessageToUser(User user, String message)
    {
        user.sendMessage(message);
    }

    public static void sendIssueMessage(int id, String comment)
    {
        try
        {
            SparkyForge.getGitbot().getIssue(id).comment(comment);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void postMessageTwitterMessage(String message)
    {
        SparkyForge.getTwitBot().postTweet(message);
    }

//    public static void sendIssueMessage(int id, String comment)
//    {
//        try
//        {
//            SparkyForge.getGitbot().getIssue(id).comment(comment);
//        } catch (IOException e)
//        {
//            e.printStackTrace();
//        }
//    }
}
