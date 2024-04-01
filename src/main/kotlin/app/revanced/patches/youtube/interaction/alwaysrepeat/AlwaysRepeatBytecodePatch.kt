package app.revanced.patches.youtube.interaction.alwaysrepeat

import app.revanced.util.exception
import app.revanced.patcher.data.BytecodeContext
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.patcher.patch.annotation.CompatiblePackage
import app.revanced.patcher.patch.annotation.Patch
import app.revanced.patches.youtube.misc.playercontrols.PlayerControlsBytecodePatch
import app.revanced.patches.youtube.video.information.VideoInformationPatch
import app.revanced.patches.youtube.interaction.alwaysrepeat.fingerprints.AutoNavInformerFingerprint
import app.revanced.patcher.util.proxy.mutableTypes.MutableMethod
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import app.revanced.patcher.extensions.InstructionExtensions.getInstruction
import app.revanced.patcher.extensions.InstructionExtensions.addInstructions

@Patch(
    name = "Always repeat video",
    description = "Adds options to display buttons in the video player to always repeat video.",
    dependencies = [
        AlwaysRepeatResourcePatch::class,
        PlayerControlsBytecodePatch::class,
        VideoInformationPatch::class,
    ],
    compatiblePackages = [
        CompatiblePackage(
            "com.google.android.youtube",
            [
                "18.48.39",
                "18.49.37",
                "19.01.34",
                "19.02.39",
                "19.03.36",
                "19.04.38",
                "19.05.36",
                "19.06.39",
                "19.07.40",
                "19.08.36",
                "19.09.37"
            ],
        ),
    ],
)
@Suppress("unused")
object AlwaysRepeatBytecodePatch : BytecodePatch(
    setOf(AutoNavInformerFingerprint)
) {
    private const val INTEGRATIONS_PLAYER_PACKAGE = "Lapp/revanced/integrations/youtube/videoplayer"
    private val BUTTONS_DESCRIPTORS = listOf(
        "$INTEGRATIONS_PLAYER_PACKAGE/AlwaysRepeatButton;",
    )

    private const val INTEGRATIONS_CLASS_DESCRIPTOR = "Lapp/revanced/integrations/youtube/patches/AlwaysRepeatPatch;"


    override fun execute(context: BytecodeContext) {
        BUTTONS_DESCRIPTORS.forEach { descriptor ->
            PlayerControlsBytecodePatch.initializeControl("$descriptor->initializeButton(Landroid/view/View;)V")
            PlayerControlsBytecodePatch.injectVisibilityCheckCall("$descriptor->changeVisibility(Z)V")
        }

        AutoNavInformerFingerprint.result?.let {
            with(
                context
                    .toMethodWalker(it.method)
                    .nextMethod(it.scanResult.patternScanResult!!.startIndex, true)
                    .getMethod() as MutableMethod
            ) {
                val index = implementation!!.instructions.size - 1 - 1
                val register = getInstruction<OneRegisterInstruction>(index).registerA

                addInstructions(
                    index + 1, """
                        invoke-static {v$register}, $INTEGRATIONS_CLASS_DESCRIPTOR;->enableAlwaysRepeat(Z)Z
                        move-result v0
                        """
                )
            }
        } ?: throw AutoNavInformerFingerprint.exception
    }
}
