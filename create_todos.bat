@echo off
for /L %%i in (1, 1, 10) do (
    todo create todo -t "Task %%i"
)
