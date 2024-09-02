package dev.ftb.mods.ftbmaterials;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class OreVariantsGenerator {
    static Path rootPath = Path.of("/Users/michael/.gradle/caches/fabric-loom/1.21/neoforge/21.0.85-beta/client-extra/assets/minecraft/");

    static Path stoneTexture = rootPath.resolve("textures/block/stone.png");
    static Path netherrackTexture = rootPath.resolve("textures/block/netherrack.png");
    static Path endStoneTexture = rootPath.resolve("textures/block/end_stone.png");
    static Path deepslateTexture = rootPath.resolve("textures/block/deepslate.png");

    static List<Path> variants = List.of(
            stoneTexture,
            netherrackTexture,
            endStoneTexture,
            deepslateTexture
    );

    static List<String> variantNames = List.of(
            "stone_ore",
            "nether_ore",
            "end_ore",
            "deepslate_ore"
    );

    static Path fromPath = Path.of("/Users/michael/Dev/work/ftb/mods/FTB-Resources/assets/ore_overlays");

    public static void main(String[] args) throws IOException {
        var overlays = Files.list(fromPath).toList();
        for (var overlayFile : overlays) {
            // Read the file
            var overlayBytes = ImageIO.read(overlayFile.toFile());

            for (var variant : variants) {
                var arrayIndex = variants.indexOf(variant);
                var variantName = variantNames.get(arrayIndex);

                // Read in the base texture
                var variantBytes = ImageIO.read(variant.toFile());

                BufferedImage combinedImage = new BufferedImage(variantBytes.getWidth(), variantBytes.getHeight(), BufferedImage.TYPE_INT_ARGB);
                Graphics g = combinedImage.createGraphics();
                g.drawImage(variantBytes, 0, 0, null);
                g.drawImage(overlayBytes, 0, 0, null);
                g.dispose();

                var outputName = overlayFile.getFileName().toString()
                        .replace("_ore_overlay", "")
                        .replace(".png", "")
                        + "_" + variantName + ".png";

                ImageIO.write(combinedImage, "png", new File("/Users/michael/Dev/work/ftb/mods/FTB-Resources/assets/tmp/" + outputName));
            }
        }
    }
}
