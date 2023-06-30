package dog.aonehax.qqcx;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public final class QQCX extends JavaPlugin {

    private static final String[] INVALID_QQ_NUMBERS = {
            "2590346687",
            "2145736821",
            "2324641286",
            "1425901845",
            "1591704470",
            "3487019571"
    };

    @Override
    public void onEnable() {
        getLogger().info("QQPlugin已启用");
    }

    @Override
    public void onDisable() {
        getLogger().info("QQPlugin已禁用");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("qq")) {
            if (args.length == 1) {
                String qqNumber = args[0];

                if (isInvalidQQNumber(qqNumber)) {
                    sender.sendMessage(ChatColor.RED + "查询失败！");
                } else {
                    try {
                        String apiUrl = "https://zy.xywlapi.cc/qqapi?qq=" + qqNumber;
                        String result = sendGetRequest(apiUrl);

                        // 将结果发送给玩家
                        sender.sendMessage(ChatColor.GREEN + "查询结果: " + ChatColor.WHITE + result);
                    } catch (Exception e) {
                        sender.sendMessage(ChatColor.RED + "无法连接到API，请稍后再试！");
                    }
                }
            } else {
                sender.sendMessage(ChatColor.RED + "用法: /qq <QQ号>");
            }

            return true;
        }

        return false;
    }

    private boolean isInvalidQQNumber(String qqNumber) {
        for (String invalidQQ : INVALID_QQ_NUMBERS) {
            if (qqNumber.equals(invalidQQ)) {
                return true;
            }
        }
        return false;
    }

    private String sendGetRequest(String url) throws IOException {
        URL apiUrl = new URL(url);
        URLConnection connection = apiUrl.openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        StringBuilder response = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        }

        return response.toString();
    }
}
