package com.nevermore.muzhitui.activity.rongyun.message.module;

import java.util.List;

import io.rong.imkit.DefaultExtensionModule;

import io.rong.imkit.plugin.IPluginModule;
import io.rong.imlib.model.Conversation;

/**
 * Created by Simone on 2017/3/15.
 */

public class MyExtensionModule extends DefaultExtensionModule {
    private MyPlugin myPlugin;
    @Override
    public List<IPluginModule> getPluginModules(Conversation.ConversationType conversationType) {
        myPlugin=new MyPlugin();
        List<IPluginModule> pluginModules =  super.getPluginModules(conversationType);
        pluginModules.add(myPlugin);
        return pluginModules;
    }
}
