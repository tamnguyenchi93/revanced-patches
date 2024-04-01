package app.revanced.patches.youtube.interaction.alwaysrepeat.fingerprints

import app.revanced.patcher.extensions.or
import app.revanced.patches.youtube.interaction.alwaysrepeat.AlwaysRepeatResourcePatch
import app.revanced.util.patch.LiteralValueFingerprint
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.Opcode

object AutoNavInformerFingerprint : LiteralValueFingerprint(
    returnType = "V",
    accessFlags = AccessFlags.PUBLIC or AccessFlags.FINAL,
    parameters = listOf("L"),
    opcodes = listOf(
        Opcode.INVOKE_VIRTUAL,
        Opcode.MOVE_RESULT,
        Opcode.XOR_INT_2ADDR
    ),
    literalSupplier = { AlwaysRepeatResourcePatch.settingBooleanTimeRangeDialogId }
)