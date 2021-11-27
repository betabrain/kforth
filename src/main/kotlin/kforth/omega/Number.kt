package kforth.omega

object Number : Word {
  override fun handles(idiom: String): Boolean {
    return idiom.toIntOrNull() != null
  }

  override fun execute(omega: Omega, idiom: String, token: Int) {
    omega.stack.add(idiom.toInt())
  }

  override fun immediate(omega: Omega, idiom: String, token: Int) {
    val word = omega.dictionary.last() as ColonWord
    word.tokens.add(token)
    word.tokens.add(idiom.toInt())
  }
}