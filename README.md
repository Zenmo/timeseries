Exploratory code for a time series library

Concepts:

- UntypedTimeSeries as a minimal interface for a time series
- SteppedUntypedTimeSeries as a discrete time series which can be inspected 
  for its start, end and interval
- Specific interfaces for typed variants of kilowatts, kWh, unitless, etc. 
  (since Java does not have template specialization)
