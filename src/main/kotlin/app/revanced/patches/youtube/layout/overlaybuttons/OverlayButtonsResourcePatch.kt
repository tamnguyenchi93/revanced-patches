package app.revanced.patches.youtube.layout.overlaybuttons

import app.revanced.patcher.data.ResourceContext
import app.revanced.patcher.patch.ResourcePatch
import app.revanced.patcher.patch.annotation.CompatiblePackage
import app.revanced.patcher.patch.annotation.Patch
import app.revanced.patcher.patch.options.PatchOption.PatchExtensions.booleanPatchOption

// import app.revanced.patches.youtube.overlaybutton.alwaysrepeat.AlwaysRepeatPatch
// import app.revanced.patches.youtube.overlaybutton.download.hook.DownloadButtonHookPatch
// import app.revanced.patches.youtube.overlaybutton.download.pip.DisablePiPPatch
// import app.revanced.patches.youtube.utils.overridespeed.OverrideSpeedHookPatch
// import app.revanced.patches.youtube.utils.playerbutton.PlayerButtonHookPatch
// import app.revanced.patches.youtube.utils.resourceid.SharedResourceIdPatch
// import app.revanced.patches.youtube.utils.videoid.general.VideoIdPatch
import app.revanced.patches.all.misc.resources.AddResourcesPatch
import app.revanced.patches.shared.misc.settings.preference.SwitchPreference
import app.revanced.patches.youtube.misc.settings.SettingsPatch
import app.revanced.patches.youtube.misc.playercontrols.PlayerControlsBytecodePatch
import app.revanced.patches.youtube.misc.playercontrols.BottomControlsResourcePatch
import app.revanced.util.ResourceGroup
import app.revanced.util.copyResources
import app.revanced.util.copyXmlNode


@Patch(
    name = "Overlay buttons",
    description = "Adds an option to display overlay buttons in the video player.",
    dependencies = [
        SettingsPatch::class,
        // AlwaysRepeatPatch::class,
        // DisablePiPPatch::class,
        // DownloadButtonHookPatch::class,
        // OverrideSpeedHookPatch::class,
        // PlayerButtonHookPatch::class,
        // SharedResourceIdPatch::class,
        // VideoIdPatch::class
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
            ]
        )
    ]
)


@Suppress("unused")
object OverlayButtonsResourcePatch : ResourcePatch() {
    private val OutlineIcon by booleanPatchOption(
        key = "OutlineIcon",
        default = false,
        title = "Outline icons",
        description = "Apply the outline icon",
        required = true
    )

    private val WiderBottomPadding by booleanPatchOption(
        key = "WiderBottomPadding",
        default = false,
        title = "Wider bottom padding",
        description = "Apply wider bottom padding. Click effect may not be shown in the correct position."
    )

    private const val INTEGRATIONS_PLAYER_PACKAGE = "Lapp/revanced/integrations/youtube/videoplayer"
    private val OVERLAY_BUTTONS_DESCRIPTORS = listOf(
        "$INTEGRATIONS_PLAYER_PACKAGE/AlwaysRepeatButton;",
        // "$INTEGRATIONS_PLAYER_PACKAGE/CopyVideoUrlButton;",
        // "$INTEGRATIONS_PLAYER_PACKAGE/CopyVideoUrlTimestampButton;",
        // "$INTEGRATIONS_PLAYER_PACKAGE/ExternalDownload",
        // "$INTEGRATIONS_PLAYER_PACKAGE/SpeedDialog"
    )

    override fun execute(context: ResourceContext) {
        AddResourcesPatch(this::class)

        OVERLAY_BUTTONS_DESCRIPTORS.forEach { descriptor ->
            PlayerControlsBytecodePatch.initializeControl("$descriptor->initializeButton(Landroid/view/View;)V")
            PlayerControlsBytecodePatch.injectVisibilityCheckCall("$descriptor->changeVisibility(Z)V")
        }

        /**
         * Add settings
         */
        
        SettingsPatch.PreferenceScreen.OVERLAY_BUTTONS.addPreferences(
            SwitchPreference("revanced_overlay_button_always_repeat"),
            SwitchPreference("revanced_overlay_button_copy_video_url_timestamp"),
            SwitchPreference("revanced_overlay_button_copy_video_url"),
            SwitchPreference("revanced_overlay_button_external_downloader"),
            SwitchPreference("revanced_overlay_button_speed_dialog")
        )

        /**
         * Copy arrays
         */
        context.copyXmlNode("overlaybuttons/shared/host", "values/arrays.xml", "resources")

        /**
         * Copy resources
         */
        context.copyResources(
            "overlaybuttons/shared", ResourceGroup(
                resourceDirectoryName = "drawable",
                "playlist_repeat_button.xml",
                "playlist_shuffle_button.xml",
                "revanced_repeat_icon.xml"
            )
        )
        if (OutlineIcon == true) {
            context.copyResources(
                "overlaybuttons/outline", ResourceGroup(
                    resourceDirectoryName = "drawable-xxhdpi",
                    "ic_fullscreen_vertical_button.png",
                    "quantum_ic_fullscreen_exit_grey600_24.png",
                    "quantum_ic_fullscreen_exit_white_24.png",
                    "quantum_ic_fullscreen_grey600_24.png",
                    "quantum_ic_fullscreen_white_24.png",
                    "revanced_copy_icon.png",
                    "revanced_copy_icon_with_time.png",
                    "revanced_download_icon.png",
                    "revanced_speed_icon.png",
                    "yt_fill_arrow_repeat_white_24.png",
                    "yt_outline_arrow_repeat_1_white_24.png",
                    "yt_outline_arrow_shuffle_1_white_24.png",
                    "yt_outline_screen_full_exit_white_24.png",
                    "yt_outline_screen_full_white_24.png"
                )
            )
        } else {
            context.copyResources(
                "overlaybuttons/default", ResourceGroup(
                    resourceDirectoryName = "drawable-xxhdpi",
                    "ic_fullscreen_vertical_button.png",
                    "ic_vr.png",
                    "quantum_ic_fullscreen_exit_grey600_24.png",
                    "quantum_ic_fullscreen_exit_white_24.png",
                    "quantum_ic_fullscreen_grey600_24.png",
                    "quantum_ic_fullscreen_white_24.png",
                    "revanced_copy_icon.png",
                    "revanced_copy_icon_with_time.png",
                    "revanced_download_icon.png",
                    "revanced_speed_icon.png",
                    "yt_fill_arrow_repeat_white_24.png",
                    "yt_outline_arrow_repeat_1_white_24.png",
                    "yt_outline_arrow_shuffle_1_white_24.png",
                    "yt_outline_screen_full_exit_white_24.png",
                    "yt_outline_screen_full_white_24.png",
                    "yt_outline_screen_vertical_vd_theme_24.png"
                )
            )
        }


        BottomControlsResourcePatch.addControls("overlaybuttons/shared")

        /**
         * Adjust Fullscreen Button size and padding
         */
        val bottomPadding = if (WiderBottomPadding == true) "31.0dip" else "22.0dip"
        BottomControlsResourcePatch.setBottomPadding(bottomPadding)

    }
}
