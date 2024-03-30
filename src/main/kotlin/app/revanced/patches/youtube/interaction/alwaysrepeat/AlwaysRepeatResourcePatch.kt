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

@Patch(
    dependencies = [
        SettingsPatch::class,
        BottomControlsResourcePatch::class,
        AddResourcesPatch::class
    ]
)
internal object AlwaysRepeatResourcePatch : ResourcePatch() {
    override fun execute(context: ResourceContext) {
        AddResourcesPatch(this::class)

        SettingsPatch.PreferenceScreen.PLAYER.addPreferences(
            SwitchPreference("revanced_always_repeat"),
        )

        context.copyResources(
            "alwaysrepeat", ResourceGroup(
                resourceDirectoryName = "drawable",
                "revanced_yt_alwaysrepeat.xml",
            )
        )

        BottomControlsResourcePatch.addControls("alwaysrepeat")
    }
}