package app.revanced.patches.youtube.interaction.alwaysrepeat

import app.revanced.patcher.data.ResourceContext
import app.revanced.patcher.patch.ResourcePatch
import app.revanced.patcher.patch.annotation.Patch
import app.revanced.patches.all.misc.resources.AddResourcesPatch
import app.revanced.patches.shared.misc.settings.preference.SwitchPreference
import app.revanced.patches.youtube.misc.playercontrols.BottomControlsResourcePatch
import app.revanced.patches.youtube.misc.settings.SettingsPatch
import app.revanced.util.ResourceGroup
import app.revanced.util.copyResources
import app.revanced.patches.shared.misc.mapping.ResourceMappingPatch

@Patch(
    dependencies = [
        SettingsPatch::class,
        BottomControlsResourcePatch::class,
        AddResourcesPatch::class
    ]
)
internal object AlwaysRepeatResourcePatch : ResourcePatch() {
    internal var settingBooleanTimeRangeDialogId = -1L


    override fun execute(context: ResourceContext) {
        AddResourcesPatch(this::class)

        SettingsPatch.PreferenceScreen.PLAYER.addPreferences(
            SwitchPreference("revanced_always_repeat"),
        )

        // literalSupplier = { SettingsBooleanTimeRangeDialog } setting_boolean_time_range_dialog
        settingBooleanTimeRangeDialogId = ResourceMappingPatch.resourceMappings.single {
            it.type == "layout" && it.name == "setting_boolean_time_range_dialog$name"
        }.id

        context.copyResources(
            "alwaysrepeat", ResourceGroup(
                resourceDirectoryName = "drawable",
                "revanced_yt_alwaysrepeat.xml",
                "revanced_yt_outlined_alwaysrepeat.xml"
            )
        )

        BottomControlsResourcePatch.addControls("alwaysrepeat")
    }
}