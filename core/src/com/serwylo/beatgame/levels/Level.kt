package com.serwylo.beatgame.levels

import com.badlogic.gdx.utils.I18NBundle
import com.serwylo.beatgame.levels.achievements.Achievement

data class Level(
        val mp3Name: String,
        val labelId: String,
        val unlockRequirements: UnlockRequirements
)

object Levels {

    val TheLaundryRoom = Level(
            "the_haunted_mansion_the_laundry_room.mp3",
            "levels.the-laundry-room",
            Unlocked()
    )

    val TheCourtyard = Level(
            "the_haunted_mansion_the_courtyard.mp3",
            "levels.the-courtyard",
            Unlocked()
    )

    val Maintenance = Level(
            "health_and_safety_maintenance.mp3",
            "levels.maintenance",
            TotalAchievements(5)
    )

    val ForcingTheGamecard = Level(
            "health_and_safety_forcing_the_gamecard.mp3",
            "levels.forcing-the-gamecard",
            TotalAchievements(10)
    )

    val SharplyBentWire = Level(
            "health_and_safety_sharply_bent_wire.mp3",
            "levels.sharply-bent-wire",
            TotalAchievements(15)
    )

    val EyeTwitching = Level(
            "health_and_safety_eye_twitching.mp3",
            "levels.eye-twitching",
            TotalAchievements(20)
    )

    val LightFlashes = Level(
            "health_and_safety_light_flashes.mp3",
            "levels.light-flashes",
            TotalAchievements(25)
    )

    val PlayInAWellLitRoom = Level(
            "health_and_safety_play_in_a_well_lit_room.mp3",
            "levels.play-in-a-well-lit-room",
            TotalAchievements(30)
    )

    val ContactWithMoistureAndDirt = Level(
            "health_and_safety_contact_with_moisture_and_dirt.mp3",
            "levels.contact-with-moisture-and-dirt",
            TotalAchievements(35)
    )

    val TheBallroom = Level(
            "the_haunted_mansion_the_ballroom.mp3",
            "levels.the-ballroom",
            TotalAchievements(40)
    )

    val OldClock = Level(
            "awakenings_old_clock.mp3",
            "levels.old-clock",
            TotalAchievements(45)
    )

    val RegulationsForEquipment = Level(
            "health_and_safety_regulations_for_equipment_use.mp3",
            "levels.regulations-for-equipment",
            TotalAchievements(50)
    )

    val Convulsions = Level(
            "health_and_safety_convulsions.mp3",
            "levels.convulsions",
            TotalAchievements(55)
    )

    val ContactWithDustAndLint = Level(
            "health_and_safety_contact_with_dust_and_lint.mp3",
            "levels.contact-with-dust-and-lint",
            TotalAchievements(60)
    )

    val TheExerciseRoom = Level(
            "the_haunted_mansion_the_exercise_room.mp3",
            "levels.the-exercise-room",
            TotalAchievements(65)
    )

    val Vivaldi = Level(
            "vivaldi.mp3",
            "levels.vivaldi",
            TotalAchievements(75)
    )

    val ReorientTheReceivingAntenna = Level(
            "health_and_safety_reorient_the_receiving_antenna.mp3",
            "levels.reorient-the-receiving-antenna",
            TotalAchievements(80)
    )

    val Custom  = Level(
            "custom.mp3",
            "levels.custom",
            Unlocked()
    )

    val all = listOf(
            TheLaundryRoom,
            TheCourtyard,
            Maintenance,
            ForcingTheGamecard,
            SharplyBentWire,
            EyeTwitching,
            LightFlashes,
            PlayInAWellLitRoom,
            ContactWithMoistureAndDirt,
            TheBallroom,
            OldClock,
            RegulationsForEquipment,
            Convulsions,
            ContactWithDustAndLint,
            TheExerciseRoom,
            Vivaldi,
            ReorientTheReceivingAntenna,
            Custom
    )

    fun bySong(mp3Name: String): Level {
        return all.find { it.mp3Name == mp3Name }
                ?: all[17]
    }

}

abstract class UnlockRequirements {
    abstract fun isLocked(achievements: List<Achievement>): Boolean
    abstract fun isAlmostUnlocked(achievements: List<Achievement>): Boolean
    abstract fun describeOutstandingRequirements(strings: I18NBundle, achievements: List<Achievement>): String
}

class Unlocked: UnlockRequirements() {
    override fun isLocked(achievements: List<Achievement>) = false
    override fun isAlmostUnlocked(achievements: List<Achievement>) = false
    override fun describeOutstandingRequirements(strings: I18NBundle, achievements: List<Achievement>) = ""
}

class TotalAchievements(val numRequired: Int, val numUntilAlmostUnlocked: Int = 10): UnlockRequirements() {
    override fun isLocked(achievements: List<Achievement>): Boolean {
        return achievements.size < numRequired
    }

    override fun isAlmostUnlocked(achievements: List<Achievement>): Boolean {
        return numRequired - achievements.size <= numUntilAlmostUnlocked
    }

    override fun describeOutstandingRequirements(strings: I18NBundle, achievements: List<Achievement>): String {
        val numLeft = numRequired - achievements.size
        return strings.format("achievements.num-left", numLeft)
    }
}
