---
description: Review a plan across architecture, code quality, tests, and performance before coding
---

# Review Plan

Review the current plan before writing code.

$ARGUMENTS

## Ground rules

- Check the plan against `AGENTS.md`, especially **Banned patterns**.
- Prefer the smallest complete change; flag anything the task does not need.
- Reuse existing helpers before adding new ones.
- Tests: one good test for non-trivial logic, not coverage.
- Handle more edge cases, not fewer; explicit over clever.

## Workflow

1. Ask the user to choose:
   - **BIG CHANGE** — one section at a time, up to 4 issues per section.
   - **SMALL CHANGE** — one question per section.
2. Work through the four sections below in order.
3. After each section, pause for feedback before continuing.
4. For every issue:
   - Describe the problem with file and line references.
   - Present 2-3 options, including "do nothing" when reasonable.
   - For each option: effort, risk, impact on other code, maintenance burden.
   - Recommend one option and explain why.
   - Ask whether the user agrees before proceeding.

## 1. Architecture

- **Boundaries** — clear components with one responsibility.
- **Dependencies** — Koin singletons, Navigation 3 entries, Flutter engine ownership.
- **Data flow** — one source of truth; no duplicated state.
- **Lifecycle** — Flutter view across config changes and screen exit.
- **Security** — exported components, intents, launcher shortcuts.

## 2. Code quality

- **Placement** — screens in `ui/`, widgets in `widgets/`, tokens in `ui/theme/`.
- **Duplication** — copy-paste logic and repeated values.
- **Errors** — handled at the right level; no silent failures.
- **Debt** — dead code, TODOs, over- or under-engineering.

## 3. Tests

- **Coverage** — risky logic has a test that fails when it breaks.
- **Flutter** — `FlutterScreenTest` and config-change behavior.
- **Quality** — edge cases, failure paths, behavior-focused assertions.

## 4. Performance

- **Recomposition** — state reads and unnecessary work.
- **Main thread** — blocking IO or CPU.
- **Images** — loading, caching, memory.
- **Flutter** — engine startup and view recreation.
- **Startup** — cost and deferred work.
