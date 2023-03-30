package skychat.events;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import skychat.skychat.SkyChat;

public class PlayerChat implements Listener {
    SkyChat plugin;

    public PlayerChat(SkyChat skyChat) {
        plugin = skyChat;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    void OnPlayerChat(AsyncChatEvent e)
    {
        if (e.isCancelled())
            return;

        String msg = PlainTextComponentSerializer.plainText().serialize(e.message());
        String displayName = PlainTextComponentSerializer.plainText().serialize(e.getPlayer().displayName());

        if (!e.getPlayer().hasPermission("skychat.useCustomFont") && !plugin.skyfont.isMessageValid(msg))
        {
            e.getPlayer().sendMessage(ChatColor.RED + "[SkyNeoxx] Votre texte contient des charactères interdits.\nPetit filou !");
            e.setCancelled(true);

            return;
        }

        msg = replaceCurrencySymbols(msg);

        Component c = (Component.text(getPrefix(e.getPlayer()) + " " + displayName + " : ")
                .hoverEvent(HoverEvent.showText(getTextInfosPanel(e.getPlayer())))
                .clickEvent(ClickEvent.suggestCommand("/info " + e.getPlayer().getName()))
                .append(Component.text(msg))
                );

        plugin.getServer().broadcast(c);

        e.setCancelled(true);
    }

    String replaceCurrencySymbols(String s)
    {
        return s.replace("$", plugin.skyfont.getCharacter("money")).replace("€", plugin.skyfont.getCharacter("money"));
    }

    String getPrefix(Player player)
    {
        String group = plugin.chat.getPlayerGroups(player)[0];
        if (group.isEmpty())
            return "";

        String prefix = plugin.chat.getGroupPrefix(player.getWorld(), group);
        prefix = prefix.replaceAll("&", "§");

        return prefix;
    }

    Component getTextInfosPanel(Player player)
    {
        String displayName = PlainTextComponentSerializer.plainText().serialize(player.displayName());

        return Component.text("       " + ChatColor.DARK_GREEN + ChatColor.BOLD + displayName +
                "\n" + ChatColor.WHITE + ChatColor.BOLD + "> " + ChatColor.RED + "Rang " + ChatColor.GRAY + "- " + "§7Fer" +
                "\n" + ChatColor.WHITE + ChatColor.BOLD + "> " + ChatColor.RED + "Grade " + ChatColor.GRAY + "- " + getPrefix(player) +
                "\n\n" + ChatColor.WHITE + ChatColor.BOLD + "> " + ChatColor.RED + "Niveau de l'île " + ChatColor.GRAY + "- " + ChatColor.DARK_GREEN + "297.45" +
                "\n" + ChatColor.WHITE + ChatColor.BOLD + "> " + ChatColor.RED + "Classement de l'île " + ChatColor.GRAY + "- " + ChatColor.DARK_GREEN + "967" +
                "\n\n" + ChatColor.WHITE + ChatColor.BOLD + "> " + ChatColor.RED + "Coins " + ChatColor.GRAY + "- " + ChatColor.DARK_GREEN + "6754.34" + ChatColor.WHITE + plugin.skyfont.getCharacter("money") +
                "\n" + ChatColor.WHITE + ChatColor.BOLD + "> " + ChatColor.RED + "SkyCoins " + ChatColor.GRAY + "- " + ChatColor.DARK_GREEN + "55" + ChatColor.WHITE + plugin.skyfont.getCharacter("skycoins"));
    }
}
