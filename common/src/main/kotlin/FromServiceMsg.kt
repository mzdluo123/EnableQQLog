package io.github.mzdluo123.enableqqlog

import com.google.gson.annotations.SerializedName

@Suppress("ArrayInDataClass")
data class FromServiceMsg(
    @SerializedName("resultCode")
    val resultCode: Int = 0,
    @SerializedName("serviceCmd")
    val serviceCmd: String = "",
    @SerializedName("ssoSeq")
    val ssoSeq: Int = 0,
    @SerializedName("wupBuffer")
    val wupBuffer: ByteArray = byteArrayOf(),

    // @SerializedName("appId")
    // val appId: Int = 0,
    // @SerializedName("appSeq")
    // val appSeq: Int = 0,
    // @SerializedName("attributes")
    // val attributes: Attributes = Attributes(),
    // @SerializedName("errorMsg")
    // val errorMsg: String = "",
    // @SerializedName("extraData")
    // val extraData: ExtraData = ExtraData(),
    // @SerializedName("flag")
    // val flag: Int = 0,
    // @SerializedName("fromVersion")
    // val fromVersion: Int = 0,
    // @SerializedName("msfCommand")
    // val msfCommand: String = "",
    // @SerializedName("msgCookie")
    // val msgCookie: List<Int> = listOf(),
    // @SerializedName("uin")
    // val uin: String = "",
) {
    class Attributes

    data class ExtraData(
        @SerializedName("mAllowFds")
        val mAllowFds: Boolean = false,
        @SerializedName("mClassLoader")
        val mClassLoader: MClassLoader = MClassLoader(),
        @SerializedName("mFdsKnown")
        val mFdsKnown: Boolean = false,
        @SerializedName("mHasFds")
        val mHasFds: Boolean = false,
        @SerializedName("mMap")
        val mMap: MMap = MMap(),
    ) {
        data class MClassLoader(
            @SerializedName("packages")
            val packages: Packages = Packages(),
            @SerializedName("proxyCache")
            val proxyCache: ProxyCache = ProxyCache(),
        ) {
            data class Packages(
                @SerializedName("com.android.org.conscrypt")
                val comAndroidOrgConscrypt: ComAndroidOrgConscrypt = ComAndroidOrgConscrypt(),
            ) {
                data class ComAndroidOrgConscrypt(
                    @SerializedName("implTitle")
                    val implTitle: String = "",
                    @SerializedName("implVendor")
                    val implVendor: String = "",
                    @SerializedName("implVersion")
                    val implVersion: String = "",
                    @SerializedName("name")
                    val name: String = "",
                    @SerializedName("specTitle")
                    val specTitle: String = "",
                    @SerializedName("specVendor")
                    val specVendor: String = "",
                    @SerializedName("specVersion")
                    val specVersion: String = "",
                )
            }

            class ProxyCache
        }

        data class MMap(
            @SerializedName("version")
            val version: Int = 0,
        )
    }
} 