# Smart Laundry Facility Simulation

This repository contains a Java Swing simulation for the CT074-3-2 Concurrent Programming Smart Laundry Facility case study.

## Implemented simulation behaviour
- 50 independently submitted customer tasks arrive at random 0–3 second intervals.
- Six fair washer permits, four fair dryer permits, and two fair payment-kiosk permits control access.
- Washing takes 4–6 seconds, drying 3–5 seconds, and payment 1–2 seconds.
- A 5% washer failure retries the wash; a 5% kiosk failure retries payment after two seconds.
- The dashboard shows individual busy machines plus served, average journey time, and maximum concurrent washer/dryer use.
- The congested scenario makes kiosks unavailable for the day and alerts the owner exactly once once 30 customers are queued.

## Concurrency design
- Fair `Semaphore`s model a finite number of equivalent physical resources and reduce starvation.
- A `ReentrantLock` makes each scan-and-claim of a visual machine slot mutually exclusive.
- `AtomicInteger`/`AtomicLong` collect counters and use compare-and-set to maintain maxima safely.
- A `volatile` broken-kiosk flag gives worker threads visibility of the scenario state.
- A cached `ExecutorService` executes customer tasks and supports orderly interruption with `shutdownNow()`.

## Project structure
- `src/laundry/model`: shared facility, customer task, and statistics model.
- `src/laundry/gui`: Swing visualisation.
- `src/laundry/Main.java`: laundry simulation entry point.

## Compile and run
```bash
javac -d out $(find src -name "*.java")
java -cp out laundry.Main
```

Stop a run with **Stop**; it interrupts any waiting customer tasks and releases all acquired resources via `finally` blocks.
