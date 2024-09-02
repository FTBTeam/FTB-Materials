package dev.ftb.tooling

import java.awt.Graphics
import java.awt.image.BufferedImage
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import javax.imageio.ImageIO

val variantNames = listOf(
    "stone_ore",
    "nether_ore",
    "end_ore",
    "deepslate_ore"
)

/**
 * Takes a set of overlay textures and applies them to a set of base textures to generate a set of variant textures.
 */
fun main(args: Array<String>) {
    if (args.size != 3) {
        println("Usage: OreStoneVariantsGeneratorKt <path to minecraft resources> <path to overlay textures> <path to output directory>")
        return
    }

    val texturesRoot = Path.of(args[0])
    val overlaysRoot = Path.of(args[1])
    val outputRoot = Path.of(args[2])

    val variants = listOf(
        texturesRoot.resolve("textures/block/stone.png"),
        texturesRoot.resolve("textures/block/netherrack.png"),
        texturesRoot.resolve("textures/block/end_stone.png"),
        texturesRoot.resolve("textures/block/deepslate.png")
    )

    val overlays = Files.list(overlaysRoot).toList()
    for (overlayFile in overlays) {
        // Read the file
        val overlayBytes = ImageIO.read(overlayFile.toFile())

        for (variant in variants) {
            val arrayIndex: Int = variants.indexOf(variant)
            val variantName: String = variantNames[arrayIndex]

            // Read in the base texture
            val variantBytes = ImageIO.read(variant.toFile())

            val combinedImage = BufferedImage(variantBytes.width, variantBytes.height, BufferedImage.TYPE_INT_ARGB)
            val g: Graphics = combinedImage.createGraphics()
            g.drawImage(variantBytes, 0, 0, null)
            g.drawImage(overlayBytes, 0, 0, null)
            g.dispose()

            val outputName = (overlayFile.fileName.toString()
                .replace("_ore_overlay", "")
                .replace(".png", "")
                    + "_" + variantName + ".png")

            if (Files.notExists(outputRoot)) {
                Files.createDirectories(outputRoot)
            }

            ImageIO.write(
                combinedImage,
                "png",
                File("$outputRoot/$outputName")
            )
        }
    }
}

