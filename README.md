# Genetic Algorithm for Hub Location Problem

> [!TODO]
> Describe the problem and project

> [!NOTE]
> This project uses `just` (an alternative of `make`) to ease
> the commands to execute the project. If you don't want to install
> this, check the [Justfile](./Justfile) for the commands executed by
> the recipe.

## Generating problems

The problems of the project are generated using the phub problems compiled in the
[OR-library](https://people.brunel.ac.uk/~mastjjb/jeb/orlib/phubinfo.html) of
[J.E. Beasley](http://people.brunel.ac.uk/~mastjjb/jeb/jeb.html). Different subproblems
can be generated from the Australian Post data with the `generate.c` file given in the
library. To ease this just run the following recipe specifying the number of nodes and
hubs. The generated subproblem will be placed in the resources folder where the project
will be able to pick it.

```sh
just subproblem 10 3
```

## Running the algorithm

To execute the algorithm a single time until the stop criterion is met you can just run 
the recipe with N and P.

```sh
just run "10 3"
```

It is also possible to specify a seed, a maximum number of evaluations and the population
size.

```sh
just run "10 3 --seed 123 --limit 10000 --population 15"
```