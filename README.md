# Soft Computing Utilities

A modular Java library for building and experimenting with soft-computing techniques: Genetic Algorithms, Fuzzy Logic, and Neural Networks.

This repository contains reusable implementations and example use-cases (including a neuroevolution racing demo). The codebase targets Java 17 and uses Maven for build and dependency management.

---

## Table of Contents

- [Overview](#overview)
- [Requirements](#requirements)
- [Build & Run](#build--run)
- [Examples](#examples)
- [Project structure](#project-structure)
- [Documentation](#documentation)
- [Tests](#tests)
- [Contributing](#contributing)
- [License](#license)

---

## Overview

The project implements three main soft-computing paradigms:

- **Genetic Algorithms (GA)** — core classes, selection/crossover/mutation/replacement operators, and chromosome factories.
- **Fuzzy Logic** — fuzzy sets, membership and inference utilities.
- **Neural Networks** — small feed-forward neural network utilities used together with GA for neuroevolution.

Modules are designed to be reusable and combinable for experiments and small research projects.

## 📚 Documentation

**[View API Documentation (Javadoc)](https://callmeess.github.io/soft-computing-util/)**

The complete API documentation is automatically generated and hosted on GitHub Pages. It includes:
- Comprehensive package overviews
- Detailed class and interface documentation
- Usage examples and code snippets
- Method-level documentation with parameters and return types

The documentation is updated automatically with each push to the repository.
>>>>>>> be3bb3d (Setup GitHub Pages for Javadoc deployment)

## Requirements

- Java 17 (JDK 17)
- Maven 3.6+ (or the included wrapper inside the `soft-util` module)
- A desktop environment for GUI examples (RaceSimulation uses Swing)

Check your Java version with:

```bash
java -version
```

## Build & run

The project is a Maven module named `soft-util` inside this repository. There is a Maven wrapper (`mvnw`) located in the `soft-util` folder.

From the repository root you can build the module like this:

```bash
cd soft-util
./mvnw -DskipTests package
```

Or, if you use a system Maven installation:

```bash
cd soft-util
mvn -DskipTests package
```

Run the interactive GA examples (console) using the provided main class:

```bash
mvn -pl soft-util exec:java -Dexec.mainClass="com.example.softcomputing.genetic.SoftUtilApplication"
```

Run the neuroevolution race simulation (GUI):

```bash
mvn -pl soft-util exec:java -Dexec.mainClass="com.example.softcomputing.usecase.simulation.RaceSimulation"
```

Run the fuzzy logic demo (`FuzzyApp`)
-----------------------------------

The repository includes a small runnable demo for the fuzzy subsystem: `com.example.softcomputing.fuzzy.FuzzyApp`.

from the `soft-util` directory you can run the demo directly with system Maven :

```bash
mvn exec:java -Dexec.mainClass="com.example.softcomputing.fuzzy.FuzzyApp"
```
Or using the included Maven wrapper from the module directory:

```bash
./mvnw exec:java -Dexec.mainClass="com.example.softcomputing.fuzzy.FuzzyApp"
```

### Run the recommendation system

The repository includes a simple recommendation runner that scores and ranks products per user using the fuzzy subsystem: `com.example.softcomputing.fuzzy.RecommendationRunner`.

From the `soft-util` module directory you can run it with the Maven wrapper:

```bash
./mvnw exec:java -Dexec.mainClass="com.example.softcomputing.fuzzy.RecommendationRunner"
```

Or with system Maven:

```bash
mvn -pl soft-util exec:java -Dexec.mainClass="com.example.softcomputing.fuzzy.RecommendationRunner"
```

Optional arguments (in order):
- product CSV path (default: auto-detected common locations)
- user CSV path (default: auto-detected common locations)
- output directory (default: `output` in current working directory)

Example passing explicit CSVs and output dir:

```bash
./mvnw exec:java -Dexec.mainClass="com.example.softcomputing.fuzzy.RecommendationRunner" -Dexec.args="src/main/java/com/example/softcomputing/fuzzy/data/product.csv src/main/java/com/example/softcomputing/fuzzy/data/person.csv output"
```

After the run, a CSV file will be written per user (e.g. `user_1_recommendations.csv`) in the chosen output directory.

Notes:
- The project contains a Maven Shade plugin configured to produce a shaded jar, but the `mainClass` in the pom currently points to `com.example.soft_util.SoftUtilApplication` (underscored package name) which does not match the real package. If you prefer an executable jar, I can update the `pom.xml` to set the correct `mainClass` to `com.example.softcomputing.genetic.SoftUtilApplication`.
- Using `exec:java` (shown above) avoids the shade configuration issue and runs the classes directly from the build output.

## Examples

- Interactive console: `com.example.softcomputing.genetic.SoftUtilApplication` — choose chromosomes and run built-in test cases.
- GUI simulation: `com.example.softcomputing.usecase.simulation.RaceSimulation` — opens a Swing window showing neuroevolution racing demo (training/inference modes).
- Useful helper: `com.example.softcomputing.utils.TestCases` contains preconfigured GA test scenarios invoked by the console application.

## Project structure

Top-level module: `soft-util`

- `src/main/java/com/example/softcomputing/genetic` — GA core, operators, chromosome implementations and factories
- `src/main/java/com/example/softcomputing/neuralnetwork` — small feed-forward NN implementation
- `src/main/java/com/example/softcomputing/fuzzy` — fuzzy logic utilities
- `src/main/java/com/example/softcomputing/usecase/simulation` — example simulation (RaceSimulation) and related utilities
- `src/test` — tests and fitness function examples
 - `docs/` — documentation and diagrams
	- Markdown files:
		- [fuzzy.md](docs/fuzzy.md) — notes about fuzzy subsystem
		- [genetic-algo.md](docs/genetic-algo.md) — design notes for genetic algorithm module
		- [NN.md](docs/NN.md) — neural network notes
	- Diagrams:
		- [class-diagram.mermaid](docs/class-diagram.mermaid)
		- [diagrams](docs/diagrams/) — supporting diagram files

## Documentation

Additional notes and diagrams are in the `docs/` folder. See:

- `docs/fuzzy.md` — notes about fuzzy subsystem
- `docs/genetic-algo.md` — design notes for genetic algorithm module
- `docs/NN.md` — neural network notes
- `docs/class-diagram.mermaid` and `docs/diagrams/` — architecture diagrams

## Tests

Run unit tests for the module:

```bash
cd soft-util
./mvnw test
```

Or with system Maven:

```bash
mvn test -pl soft-util
```

## Contributing

Contributions are welcome. Suggested workflow:

1. Fork the repository and create a feature branch.
2. Run and add unit tests for new behavior.
3. Open a pull request with a clear description and rationale.


## License

This repository includes a `LICENSE` file at the project root. Please refer to it for license terms.

---