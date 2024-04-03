package app.revanced.patches.youtube.interaction.alwaysrepeat

import app.revanced.patcher.data.BytecodeContext
import app.revanced.patcher.extensions.InstructionExtensions.addInstructions
import app.revanced.patcher.extensions.InstructionExtensions.getInstruction
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.patcher.util.proxy.mutableTypes.MutableMethod
import app.revanced.patches.youtube.interaction.alwaysrepeat.fingerprints.AutoNavInformerFingerprint
import app.revanced.util.exception
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction

object AlwaysRepeatPatch : BytecodePatch(
    setOf(AutoNavInformerFingerprint)
) {
    internal var filterBarHeightId = -1L
    private const val INTEGRATIONS_PLAYER_PACKAGE = "Lapp/revanced/integrations/youtube/patches/AlwaysRepeatPatch"

    override fun execute(context: BytecodeContext) {

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
                        invoke-static {v$register}, $INTEGRATIONS_PLAYER_PACKAGE;->enableAlwaysRepeat(Z)Z
                        move-result v0
                        """
                )
            }
        } ?: throw AutoNavInformerFingerprint.exception

    }
}
