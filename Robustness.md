# Robustness

## Status

To improve the robustness of the system, our team is proposing to implement a SQL table of jobs in our backend database that can catch and handle errors.

## Context

Hardware failures and/or program craches can result in data loss, data corruption, or delays.

This would do a great deal of damage to our clients.

## Decision

Backend database stores and updates information for an order, the assigned drone, current status of delivery, time, and etc.

This database would be scheduled for backup regularly to keep the data safe.   

## Consequences

In the case of hardware failures or program crashes, the jobs information can be restored from the backend database. 

If the database itself crashes, it can be recovered from a backup copy.
