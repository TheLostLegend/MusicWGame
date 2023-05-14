package com.serwylo.beatgame.network.data

import java.time.Duration


data class TrackModel(
    var trackID:Int? = null ,
    var title:String?="" ,
    var duration: Int = 0 ,
    var levelMap:String?=""
)