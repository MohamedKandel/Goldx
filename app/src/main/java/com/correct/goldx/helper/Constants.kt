package com.correct.goldx.helper

object Constants {
    const val SOURCE = "come from"
    const val CITIES_BASE_URL = "https://countriesnow.space/"
    const val API_BASE_URL = "http://goldxjewelsapi.somee.com/"
    const val CUSTOMER_ID = "9fe805e9-d634-4080-9325-ee19ed51f261"
    const val OBJECT = "object"
    const val TOKEN_KEY = "token"
    const val TOKEN_VALUE = "Bearer"
    const val LANG = "lang"
    const val COUNTRY = "country"
    const val PROFILE_COMPLETED = "profile completed"
    const val FINAL_STEP = "final step"
    const val REMEMBER = "remember me"
    const val LOGGED_IN = "logged in"

    val MmSizes = listOf(
        14.07, 14.27, 14.48, 14.68, 14.88,
        15.09, 15.29, 15.49, 15.7, 15.9,
        16.1, 16.31, 16.51, 16.71, 16.92,
        17.12, 17.32, 17.53, 17.73, 17.93,
        18.14, 18.34, 18.54, 18.75, 18.95,
        19.15, 19.35, 19.56, 19.76, 19.96,
        20.17, 20.37, 20.57, 20.78, 20.98,
        21.18, 21.39, 21.59, 21.79,
        22.0, 22.2, 22.61, 22.81,
        23.01, 23.22, 23.42, 23.62, 23.83
    )

    val RingValues = listOf(
        88.63, 89.89, 91.21, 92.47, 93.73,
        95.06, 96.31, 97.57, 98.9, 100.16,
        101.42, 102.74, 104.0, 105.26, 106.58,
        107.84, 109.1, 110.43, 111.69, 112.94,
        114.27, 115.53, 116.79, 118.11, 119.37,
        120.63, 121.89, 123.21, 124.47, 125.73,
        127.06, 128.31, 129.57, 130.9, 132.16,
        133.42, 134.74, 136.0, 137.26, 138.58,
        139.84, 142.43, 143.69, 144.94, 146.27,
        147.53, 148.79, 150.11
    )

    val RingSizes = mapOf(
        88.63 to listOf("3", "44½", "F", "4"),
        89.89 to listOf("3¼", "44¾", "F½", "5"),
        91.21 to listOf("3½", "45½", "G", "-"),
        92.74 to listOf("3¾", "46", "G½", "6"),

        93.73 to listOf("4", "46¾", "H", "7"),
        95.06 to listOf("4¼", "47½", "H½", "-"),
        96.31 to listOf("4½", "48", "I", "8"),
        97.57 to listOf("4¾", "48¾", "J", "-"),

        98.9 to listOf("5", "49½", "J½", "9"),
        100.16 to listOf("5¼", "50", "K", "-"),
        101.42 to listOf("5½", "50½", "K½", "10"),
        102.74 to listOf("5¾", "51¼", "L", "-"),

        104.0 to listOf("6", "51¾", "L½", "11"),
        105.26 to listOf("6¼", "52½", "M", "12"),
        106.58 to listOf("6½", "53¼", "M½", "13"),
        107.84 to listOf("6¾", "53¾", "N", "-"),

        109.1 to listOf("7", "54½", "N½", "14"),
        110.43 to listOf("7¼", "55", "O", "-"),
        111.69 to listOf("7½", "55¾", "O½", "15"),
        112.94 to listOf("7¾", "56¼", "P", "-"),

        114.27 to listOf("8", "57", "P½", "16"),
        115.53 to listOf("8¼", "57½", "Q", "-"),
        116.79 to listOf("8½", "58¼", "Q½", "17"),
        118.11 to listOf("8¾", "59", "R", "-"),

        119.37 to listOf("9", "59½", "R½", "18"),
        120.63 to listOf("9¼", "60¼", "S", "-"),
        121.89 to listOf("9½", "60¾", "S½", "19"),
        123.21 to listOf("9¾", "61½", "T", "-"),

        124.47 to listOf("10", "62", "T½", "20"),
        125.73 to listOf("10¼", "62¾", "U", "21"),
        127.06 to listOf("10½", "63¼", "U½", "22"),
        128.31 to listOf("10¾", "64", "V", "-"),

        129.57 to listOf("11", "64¾", "V½", "23"),
        130.9 to listOf("11¼", "65¼", "W", "-"),
        132.16 to listOf("11½", "66", "W½", "24"),
        133.42 to listOf("11¾","66½","X","-"),

        134.74 to listOf("12", "67¼", "X½", "25"),
        136.0 to listOf("12¼", "67¾", "Y", "-"),
        137.26 to listOf("12½", "68½", "Z", "26"),
        138.58 to listOf("12¾", "69", "Z½", "-"),

        139.84 to listOf("13", "69¾", "Z+1", "27"),
        142.43 to listOf("13½", "71", "Z+2", "-"),
        143.69 to listOf("13¾", "71¾", "-", "-"),

        144.94 to listOf("14", "72¼", "Z+3", "-"),
        146.27 to listOf("14¼", "73", "-", "-"),
        147.53 to listOf("14½", "73½", "Z+4", "-"),
        148.79 to listOf("14¾", "74¼", "-", "-"),

        150.11 to listOf("15", "74¾", "Z+5", "-")
    )
}