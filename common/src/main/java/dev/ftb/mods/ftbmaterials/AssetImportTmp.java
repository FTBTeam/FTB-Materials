package dev.ftb.mods.ftbmaterials;

import java.nio.file.Files;
import java.nio.file.Path;

public class AssetImportTmp {
    static Path fromPath = Path.of("/Users/michael/Downloads/Mod textures");

    public static void main(String[] args) {
        try (var files = Files.walk(fromPath)) {
            var justPngs = files.filter(p -> p.toString().endsWith(".png")).toList();

            for (var file : justPngs) {
                var fileName = file.getFileName().toString();
                var fixedName = fileName
                        .replaceAll("_", ":")
                        .replaceAll(" ", ":")
                        .replaceAll(":", "_");

                fixedName = fixedName.toLowerCase();
                var outPath = Path.of("/Users/michael/Dev/work/ftb/mods/FTB-Resources/common/src/main/resources/assets/ftbmaterials/textures");
                if (fixedName.contains("block")) {
                    outPath = outPath.resolve("block");
                } else {
                    outPath = outPath.resolve("item");
                }

                Files.write(outPath.resolve(fixedName), Files.readAllBytes(file));
                System.out.println(fixedName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
