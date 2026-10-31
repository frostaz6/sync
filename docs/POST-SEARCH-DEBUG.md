# Temporary post-search error diagnostics

ReSync's legacy post-search endpoint sometimes responds HTTP 403 for the
website-session bearer. This debug build intentionally does not change the
search endpoint, its authorization, sorting, pagination or retry behavior.

For failed post/subreddit search requests, capture the raw Volley network
response before the base request's error wrapping. Display an Android dialog
containing a copyable, bounded report of the GET URL, HTTP status, error
class, time, safe response-header allowlist and the first 6000 characters of
a redacted response body. No request headers, cookies or token values
are included. Users should still review the report for personal search terms.

To reproduce: search 'nomad', wait for the 403 popup, press Copy, then paste
into the conversation. Remove this debug instrumentation after diagnosis.
Published v1.0.0 remains unchanged.
