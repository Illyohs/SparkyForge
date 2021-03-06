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

package us.illyohs.sparkyforge;

import us.illyohs.sparkyforge.bots.irc.IrcBot;
import us.illyohs.sparkyforge.bots.twitter.TwitBot;
import us.illyohs.sparkyforge.hooker.HookLoader;
import us.illyohs.sparkyforge.util.ConfigUtil;
import us.illyohs.sparkyforge.util.GitHubHelper;

public class SparkyForge
{
    private static GitHubHelper gitbot;
    private static IrcBot       ircbot;
    private static TwitBot      twitBot;
    private static HookLoader   loader = new HookLoader();



    public static void main(String... args)
    {
        loader.loadHooks();

        ircbot = new IrcBot(
                ConfigUtil.getIrcNetwork(),
                ConfigUtil.getIrcPort(),
                ConfigUtil.getIrcNick(),
                ConfigUtil.getIrcPass(),
                ConfigUtil.getIrcChannel()
        );

        ircbot.connect();

        if (ConfigUtil.isTwitterBotEnabled()) {
            twitBot = new TwitBot(
                    ConfigUtil.getTwitConsumerSecret(),
                    ConfigUtil.getTwitConsumerKey(),
                    ConfigUtil.getTwitAccToken(),
                    ConfigUtil.getTwitAccTokenSecret()
            );
        }

    }

    public static IrcBot getIrcbot()
    {
        return ircbot;
    }

    public static GitHubHelper getGitbot()
    {
        return new GitHubHelper(ConfigUtil.getGHToken(), ConfigUtil.getGHRepo());
    }

    public static TwitBot getTwitBot()
    {
        return twitBot;
    }
}
