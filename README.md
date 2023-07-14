# OOPSLA 23 Artifact

This document provides evaluation guidelines for **Paper #374: Initializing Global Objects: Time and Order**.

The artifact includes an implementation of the global object initialization checking algorithm described in the paper, integrated into the Dotty Scala compiler. This document helps verify the following:
- The initialization checker produces warnings for all code snippets the paper claims the algorithm should reject and no warnings for all code snippets the paper claims the algorithm should accept.
- The case study in section 6 can be reproduced.

## Getting Started

The artifact is a Docker image. The reviewer is invited to install Docker and make sure it is working normally. The following official guide can help with the installation of Docker: (https://docs.docker.com/get-docker/)[https://docs.docker.com/get-docker/].

**Memory Requirement:** Normally the default configuration just works. In case there are memory problems, please set the memory quota for the VM to 4GB.

Once Docker is installed on the system:

1. **Download the Docker image:** `docker pull qata/oopsla23-artifact:latest`
2. **Run the Docker image:** `docker run -it qata/oopsla23-artifact:latest`

### Play with Examples

Test cases can be found in the directory `/home/dotty/tests/init-global`:

- Test cases in `tests/init-global/pos` are expected to pass the checker with no warnings.
- Test cases in `tests/init-global/neg` are expected to to be rejected by the checker with warnings.

We can run the global initialization checker on a test file as follows:
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

## Verify Code Snippets in the Paper

All code snippets from the paper are located in `/home/dotty/code-snippets`.
The file name of a snippet has the form `2.3-a-neg.scala`, which can be read as follows:

- `2.3` means that the snippet comes from **Section 2.3** of the paper.
- `a` tells that it is the **1st** snippet in the corresponding section.
- `neg` indicates that the snippet is expected to be **rejected** by the checker with warnings.

Correspondingly, `b` and `c` are used to refer to the **2nd** and **3rd** code snippets in the paper respectively, and so on.
The suffix `pos` indicates that the snippet is expected to **pass** the checker with no warnings.

We can check a code snippet as follows:
```
$ /home/dotty/bin/scalac -Ysafe-init-global /home/dotty/code-snippets/2.5-neg.scala
```

All snippets can be run automatically with the following command:
```
$ cd /home/dotty && ./test-all.sh
```
The script will print the results to console as a table and also place results in `/home/dotty/report.csv`. The meanings of each column are as follows:
- **Snippet Name:** The name of the snippet.
- **Expected:** Will be `warning` if the test should produce initialization warnings, and `no warning` if the test should pass the checker.
- **Actual:** Will be `warning` if the checker produced warnings on this test, and `no warning` if the it did not.
- **Status:** Will be `pass` if the Expected column matches the Actual column, and `fail` otherwise.

The Status column is expected to be `pass` for all rows.

## Reproducing the Case Study

To compile the Dotty Scala compiler with the global object initialization checker enabled, first
start the sbt console as follows:
```
cd /home/dotty && sbt
```

Then input into the SBT console:
```
compile
scala3-compiler-bootstrapped/clean
scala3-compiler-bootstrapped/compile
```

This is expected to produce 110 warnings. This includes the four types of initialization time irrelevance violations described in the paper, the violatation of partial ordering, and warnings relating to pattern matches that the checker skipped due to not yet supporting evaluation of them.

The SBT shell can be exited with `Ctrl + D` and entered again with `cd /home/dotty && sbt`.
