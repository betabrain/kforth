package kforth.omega

data class ColonWord(val name: String, val tokens: MutableList<Int> = mutableListOf<Int>()) : Word {
  override fun handles(idiom: String): Boolean {
    return idiom == name
  }

  override fun execute(omega: Omega, idiom: String, token: Int) {
    tokens.forEach { omega.dictionary[it].execute(omega, idiom, it) }
  }
}