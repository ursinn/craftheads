package me.deejayarroba.craftheads.utils;

import me.deejayarroba.craftheads.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Locale;

/**
 * For a How-To on how to use AbstractCommand see this post @ http://forums.bukkit.org/threads/195990/
 *
 * @author Goblom
 */
public abstract class AbstractCommand implements CommandExecutor {

    protected static CommandMap cmap;
    protected final String command;
    protected final String description;
    protected final List<String> alias;
    protected final String usage;
    protected final String permMessage;

    protected AbstractCommand(String command, String usage, String description) {
        this(command, usage, description, null, null);
    }

    protected AbstractCommand(String command, String usage, String description, String permissionMessage,
                              List<String> aliases) {
        this.command = command.toLowerCase(Locale.getDefault());
        this.usage = usage;
        this.description = description;
        this.permMessage = permissionMessage;
        this.alias = aliases;
    }

    private static CommandMap getCommandMap() {
        if (cmap == null) {
            try {
                final Field f = Bukkit.getServer().getClass().getDeclaredField("commandMap");
                f.setAccessible(true);
                cmap = (CommandMap) f.get(Bukkit.getServer());
                return getCommandMap();
            } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException | SecurityException e) {
                Main.getInstance().getLogger().warning(String.valueOf(e));
            }
        } else {
            return cmap;
        }
        return getCommandMap();
    }

    public void register() {
        ReflectCommand cmd = new ReflectCommand(this.command);
        if (this.alias != null) {
            cmd.setAliases(this.alias);
        }
        if (this.description != null) {
            cmd.setDescription(this.description);
        }
        if (this.usage != null) {
            cmd.setUsage(this.usage);
        }
        if (this.permMessage != null) {
            cmd.setPermissionMessage(this.permMessage);
        }
        getCommandMap().register("", cmd);
        cmd.setExecutor(this);
    }

    private static final class ReflectCommand extends Command {
        private CommandExecutor exe;

        protected ReflectCommand(String command) {
            super(command);
            exe = null;
        }

        public void setExecutor(CommandExecutor exe) {
            this.exe = exe;
        }

        public boolean execute(CommandSender sender, String commandLabel, String[] args) {
            if (exe != null) {
                exe.onCommand(sender, this, commandLabel, args);
            }
            return false;
        }
    }
}
