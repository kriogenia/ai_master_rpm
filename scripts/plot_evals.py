import matplotlib.pyplot as plt
import pandas as pd

df = pd.read_csv("../data/benchmark_evals_2024-11-14T09-27.csv")
df = df.iloc[:-19]

print(df)


def aggregate(column: str):
    agg = df.groupby([column, "mutation"]).mean().reset_index()
    agg = agg.iloc[:, [1, 0, 5, 6]]
    return agg.sort_values(["mutation", column])


line_styles = {"0.05": "-", "0.1": "--", "0.25": ":", "0.5": "-."}
fig, axs = plt.subplots(1, 2, figsize=(10, 4), sharey=True)


def plot(axis: int, agg, column: str):
    for mutation in agg["mutation"].unique():
        subset = agg[agg["mutation"] == mutation]
        axs[axis].plot(
            subset[column],
            subset["gap"],
            label=f"mutation={mutation}",
            linestyle=line_styles[str(mutation)],
        )


for i, x in enumerate(["p", "n"]):
    agg = aggregate(x)
    plot(i, agg, x)
    axs[i].set_xlabel(x)
    x_range = range(10, 50, 10) if x == "n" else range(2, 6, 1)
    print(x_range)
    axs[i].set_xticks(x_range)
    axs[i].grid(True)

axs[0].set_ylabel("Gap")
axs[1].legend()

fig.suptitle("Mutation vs Gap")
plt.tight_layout()
plt.show()
