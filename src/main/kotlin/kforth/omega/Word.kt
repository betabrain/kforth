package kforth.omega

interface Word {
  fun handles(idiom: String): Boolean
  fun execute(omega: Omega, idiom: String, token: Int)
  fun immediate(omega: Omega, idiom: String, token: Int) {
    val word = omega.dictionary.last() as ColonWord
    word.tokens.add(token)
  }
}