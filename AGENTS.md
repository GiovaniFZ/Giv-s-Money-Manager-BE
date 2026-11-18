# Business rules

## Incomes

- An income amount must be greater than zero.
- An income must have a non-blank description.
- Every income must belong to a user. Use the authenticated user as its owner.
- A category is optional; if supplied, it must exist and belong to the user
  creating or updating the income.
- An income date must be valid.
- A user may update only their own incomes.
- A user may delete only their own incomes.
