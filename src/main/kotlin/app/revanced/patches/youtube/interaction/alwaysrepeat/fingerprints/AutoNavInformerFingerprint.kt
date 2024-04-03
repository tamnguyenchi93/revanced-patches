package app.revanced.patches.youtube.interaction.alwaysrepeat.fingerprints

import app.revanced.patcher.extensions.or
// import app.revanced.patches.youtube.utils.resourceid.SharedResourceIdPatch.SettingsBooleanTimeRangeDialog
import app.revanced.util.patch.LiteralValueFingerprint
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.Opcode
import app.revanced.patches.shared.misc.mapping.ResourceMappingPatch

private fun String.layoutResourceId(type: String = "layout") =
        ResourceMappingPatch.resourceMappings.single { it.type == type && it.name == this }.id

object AutoNavInformerFingerprint : LiteralValueFingerprint(
    returnType = "V",
    accessFlags = AccessFlags.PUBLIC or AccessFlags.FINAL,
    parameters = listOf("L"),
    opcodes = listOf(
        Opcode.INVOKE_VIRTUAL,
        Opcode.MOVE_RESULT,
        Opcode.XOR_INT_2ADDR
    ),

    // SettingsBooleanTimeRangeDialog = find(LAYOUT, "setting_boolean_time_range_dialog")
    // literalSupplier = { SettingsBooleanTimeRangeDialog }
    literalSupplier = { "setting_boolean_time_range_dialog".layoutResourceId() }

)