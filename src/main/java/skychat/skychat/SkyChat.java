package skychat.skychat;

import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;
import skychat.events.PlayerChat;
import skyfont.skyfont.SkyFont;

public final class SkyChat extends JavaPlugin {

    static SkyChat plugin;
    public SkyFont skyfont;
    public Chat chat;
    public Permission perms = null;

    @Override
    public void onEnable() {
        plugin = this;

        skyfont = SkyFont.getPlugin();

        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();

        setupPermissions();

        getServer().getPluginManager().registerEvents(new PlayerChat(this), this);
        getLogger().info("SkyChat started");
    }

    @Override
    public void onDisable() {
        getLogger().info("SkyChat stopped");
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = plugin.getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }

    public static SkyChat getPlugin()
    {
        return plugin;
    }

    public static String getGradient(String text, String from, String to)
    {
        String res = "";

        int fr = Integer.parseInt(from.substring(1, 3), 16);
        int fg = Integer.parseInt(from.substring(3, 5), 16);
        int fb = Integer.parseInt(from.substring(5, 7), 16);

        int tr = Integer.parseInt(to.substring(1, 3), 16);
        int tg = Integer.parseInt(to.substring(3, 5), 16);
        int tb = Integer.parseInt(to.substring(5, 7), 16);

        int dr = tr - fr;
        int dg = tg - fg;
        int db = tb - fb;

        int drs = dr / text.length();
        int dgs = dg / text.length();
        int dbs = db / text.length();

        for (int i = 0; i < text.length(); i++)
        {
            res += ChatColor.of("#" + String.format("%02X", (fr + drs * i)) + String.format("%02X", (fg + dgs * i)) + String.format("%02X", (fb + dbs * i)));
            res += ChatColor.BOLD;
            res += text.charAt(i);
        }

        return res;
    }
}
