package com.depanker.util

import java.security.MessageDigest

/**
 * Created by depankersharma on 19/10/15.
 */
object HashCreator {
  def md5(s: String): String  = {

    val bytes = MessageDigest.getInstance("MD5").digest(s.getBytes)

    val hashCharactres  = for {
      byte <- bytes
      s = Integer.toString((byte & 0xff) + 0x100, 16).substring(1)

    } yield s

    hashCharactres.mkString
  }
}
