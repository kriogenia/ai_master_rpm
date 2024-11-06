# Genetic Algorithm for Hub Location Problem

> [!TODO]
> Describe the problem and project

> [!NOTE]
> This project uses `just` (an alternative of `make`) to ease
> the commands to execute the project. If you don't want to install
> this, check the [Justfile](./Justfile) the commands executed by
> the recipe.

## Generating problems

The problems of the project are generated using
[https://people.brunel.ac.uk/~mastjjb/jeb/orlib/files/phub1.txt].
A new `just` recipe was created to ease the generation of new
subproblems in the resources folder to easily run them with the
project. To generate a new subproblem of N nodes just run:

```sh
just subproblem {N}
```

## Running the algorithm

To execute the algorithm a single time until the stop criterion
is met you can just run the recipe:

```sh
just run
```
