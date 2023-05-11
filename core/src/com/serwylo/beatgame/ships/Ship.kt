package com.serwylo.beatgame.ships

import com.badlogic.gdx.utils.I18NBundle
import com.serwylo.beatgame.levels.Levels
import com.serwylo.beatgame.levels.achievements.Achievement

data class Ship(
    val labelId: String,
    val boost: Int,
    val health: Int,
    val armor: Int
)

object Ships {

    val Wolf = Ship(
        "ships.wolf",
        60,
        3,
        3
    )

    val Snake = Ship(
        "ships.snake",
        70,
        2,
        3
    )

    val Garmr = Ship(
        "ships.garmr",
        80,
        3,
        2
    )

    val Shark = Ship(
        "ships.shark",
        90,
        1,
        3
    )

    val Basilisk = Ship(
        "ships.basilisk",
        100,
        2,
        2
    )

    val Cerberus = Ship(
        "ships.cerberus",
        110,
        3,
        1
    )

    val Megalodon = Ship(
        "ships.megalodon",
        120,
        1,
        2
    )

    val T_rex = Ship(
        "ships.t_rex",
        130,
        2,
        1
    )

    val Hydra = Ship(
        "ships.hydra",
        140,
        1,
        1
    )

    val Fenrir = Ship(
        "ships.fenrir",
        150,
        3,
        0
    )

    val Dragon = Ship(
        "ships.gragon",
        175,
        2,
        0
    )

    val Kraken = Ship(
        "ships.kraken",
        200,
        1,
        0
    )

    val all = listOf(
        Wolf,
        Snake,
        Garmr,
        Shark,
        Basilisk,
        Cerberus,
        Megalodon,
        T_rex,
        Hydra,
        Fenrir,
        Dragon,
        Kraken

    )

    fun byShip(name: String): Ship {
        return Ships.all.find { it.labelId == name }
            ?: error("Could not find level corresponding to $name")
    }

}
