# OOPSLA 23 Artifact

This document provides evaluation guidelines for **Paper #374: Initializing Global Objects: Time and Order**.

The artifact includes an implementation of the global object initialization checking algorithm described in the paper, integrated into the Dotty Scala compiler. This document helps verify the following:
- The initialization checker produces warnings for all code snippets the paper claims the algorithm should reject.
- The initialization checker produces no warnings for all code snippets the paper claims the algorithm should accept.
- The case study in section 6 can be reproduced.

## Configuration

The artifact is a Docker image. The reviewer is invited to install Docker and make sure it is working normally. The following official guide can help with the installation of Docker: (https://docs.docker.com/get-docker/)[https://docs.docker.com/get-docker/].

**Memory Requirement:** Normally the default configuration just works. In case there are memory problems, please set the memory quota for the VM to 4GB.

Once Docker is installed on the system:

1. **Download the Docker image:** `docker pull qata/oopsla23-artifact:latest`
2. **Run the Docker image:** `docker run -it qata/oopsla23-artifact:latest`

## Play with Examples

Start the sbt console as follows:
```
cd /home/dotty && sbt
```

Compile the compiler:
```
sbt> compile
sbt> scala3-compiler-bootstrapped/compile
```

The SBT shell can be exited with `Ctrl + D` and entered again with `cd /home/dotty && sbt`.

Test cases can be found in the directory `/home/dotty/tests/init-global`. Test cases in `/home/dotty/tests/init-global/pos` are expected to pass the checker with no warnings. Test cases in `/home/dotty/tests/init-global/neg` are expected to produce warnings when evaluated by the checker.

In order to run the global initialization checker on the test file `/home/dotty/tests/init-global/neg/mutable-read7.scala`:
```
$ /home/dotty/bin/scalac -Ysafe-init-global /home/dotty/tests/init-global/neg/mutable-read7.scala
```

The compiler is expected to produce the following warning:
```
-- Warning: tests/init-global/neg/mutable-read7.scala:7:17 ---------------------
7 |  if (Positioned.debug) { // error
  |      ^^^^^^^^^^^^^^^^
  |Reading mutable state of object Positioned during initialization of object Trees.
  |Reading mutable state of other static objects is forbidden as it breaks initialization-time irrelevance. Calling trace:
  |-> object Trees:     [ mutable-read7.scala:11 ]
  |   ^
  |-> val emptyTree = new Tree  [ mutable-read7.scala:13 ]
  |                   ^^^^^^^^
  |-> class Tree extends Positioned     [ mutable-read7.scala:12 ]
  |   ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
  |-> abstract class Positioned:        [ mutable-read7.scala:6 ]
  |   ^
  |-> if (Positioned.debug) { // error  [ mutable-read7.scala:7 ]
  |       ^^^^^^^^^^^^^^^^
1 warning found
```

The reviewer is invited to play with other examples in the `/home/dotty/tests/init-global` directory.

## Running Code Snippets

All code snippets from the paper are located in `/home/dotty/code-snippets`. The file name of each snippet starts with the section the snippet is from and the number of the snippet in that section, delimited by a dot. For example, the fifth code snippet from section 2 has file name starting with `2.5`. The suffix of the file name is `-pos.scala` if the snippet is expected to not produce any warnings when evaluated by the checker. The suffix of the file name is `-neg.scala` if the checker should report warnings for that snippet. Code snippet files may differ slightly from what is presented in the paper due to small omissions for the sake of readability (e.g. imports and spacing).

Running snippets is similar to running examples from the previous section. For example, to run the checker on snippet `2.5-neg.scala`:
```
$ /home/dotty/bin/scalac -Ysafe-init-global /home/dotty/code-snippets/2.5-neg.scala
```

To run the first snippet in Appendix D:
```
$ /home/dotty/bin/scalac -Ysafe-init-global /home/dotty/code-snippets/d.1-pos.scala
```

All snippets can be run automatically with the following command while in the `/home/dotty` directory:
```
$ ./test-all.sh
```
The script will print the results to console as a table and also place results in `/home/dotty/report.csv`. The meanings of each column are as follows:
- **Snippet Name:** The name of the snippet.
- **Expected:** Will be `warning` if the test should produce initialization warnings, and `no warning` if the test should pass the checker.
- **Actual:** Will be `warning` if the checker produced warnings on this test, and `no warning` if the it did not.
- **Status:** Will be `pass` if the Expected column matches the Actual column, and `fail` otherwise.

The Status column is expected to be `pass` for all rows.

## Reproducing the Case Study

To compile the Dotty Scala compiler with the global object initialization checker enabled, input into the SBT console:
```
compile
scala3-compiler-bootstrapped/clean
scala3-compiler-bootstrapped/compile
```

This is expected to produce 110 warnings. This includes the four types of initialization time irrelevance violations described in the paper, the violatation of partial ordering, and warnings relating to pattern matches that the checker skipped due to not yet supporting evaluation of them.