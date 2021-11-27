package kforth.omega

class NativeWord(val name: String, val block: Omega.() -> Unit) : Word {
  override fun handles(idiom: String): Boolean {
    return idiom == name
  }

  override fun execute(omega: Omega, idiom: String, token: Int) {
    block.invoke(omega)
  }
}