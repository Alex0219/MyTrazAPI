package com.github.theholywaffle.teamspeak3.commands;

/*
 * #%L
 * TeamSpeak 3 Java API
 * %%
 * Copyright (C) 2014 Bert De Geyter
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

import com.github.theholywaffle.teamspeak3.api.PermissionGroupDatabaseType;
import com.github.theholywaffle.teamspeak3.commands.parameter.KeyValueParam;

public class CChannelGroupCopy extends Command {

    public CChannelGroupCopy(int sourceGroupId, int targetGroupId, PermissionGroupDatabaseType type) {
        this(sourceGroupId, targetGroupId, "name", type);
    }

    public CChannelGroupCopy(int sourceGroupId, String groupName, PermissionGroupDatabaseType type) {
        this(sourceGroupId, 0, groupName, type);
    }

    private CChannelGroupCopy(int sourceGroupId, int targetGroupId, String groupName, PermissionGroupDatabaseType type) {
        super("channelgroupcopy");
        add(new KeyValueParam("scgid", sourceGroupId));
        add(new KeyValueParam("tcgid", targetGroupId));
        add(new KeyValueParam("name", groupName));
        add(new KeyValueParam("type", type.getIndex()));
    }
}
