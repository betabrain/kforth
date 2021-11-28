# kforth
A variant of the Forth Programming Language implemented in Kotlin...
Actually mainly a collection of experiments that hopefully become a language in the future.

## Aims
Learn about Forth and Forth programming by tinkering with my own implementation of it.

- Forth aims at reducing the development effort by 90% to 99%
- Code like you write slides
- Factoring: throwing away everything that isn't justified
- Write vocabularies not programs
- 100 words to describe the problem, one line to solve it
- everyone out there develops the general solution, but nobody has the general problem

## Todos
- [ ] Flexible output mechanism
- [ ] Tests for all builtin words
- [ ] Implement extension mechanism for additional builtin words
- [ ] Kotlin FFI
- [ ] Try to use FFI for string handling
- [ ] Try to use http://turboforth.net/resources/string_library.html for string handling
- [ ] Add missing words from eForth
- [ ] Get ooeForth204 examples to run
- [ ] Look at https://github.com/zeroflag/punyforth for inspiration

## Thanks
This implementation was inspired by the following implementations:
- https://github.com/zeroflag/forth4j
- [ooeForth204](https://drive.google.com/file/d/1sOnnh8uez-9z1Gy9jHYHYhhCEOZEtFq1/view)
