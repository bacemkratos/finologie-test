#!/bin/sh

# wait-for-it.sh: wait until a specified host and port are available

TIMEOUT=15
QUIET=0
HOST=""
PORT=""
CMD=""

echoerr() { if [ "$QUIET" -ne 1 ]; then echo "$@" 1>&2; fi }

usage()
{
    cat << USAGE >&2
Usage:
    $0 host:port [-t timeout] [-q] -- command args
    -t TIMEOUT       Timeout in seconds, zero for no timeout (default 15)
    -q               Don't output any status messages
    -- COMMAND ARGS  Execute command with args after the test finishes
USAGE
    exit 1
}

wait_for()
{
    echoerr "Waiting for $HOST:$PORT..."

    for i in $(seq $TIMEOUT); do
        nc -z $HOST $PORT > /dev/null 2>&1
        result=$?
        if [ $result -eq 0 ]; then
            echoerr "$HOST:$PORT is available after $i seconds"
            return 0
        fi
        sleep 1
    done

    echoerr "Timeout occurred after waiting $TIMEOUT seconds for $HOST:$PORT"
    return 1
}

while [ $# -gt 0 ]
do
    case "$1" in
        *:* )
        HOST=$(echo "$1" | cut -d : -f 1)
        PORT=$(echo "$1" | cut -d : -f 2)
        shift 1
        ;;
        -q)
        QUIET=1
        shift 1
        ;;
        -t)
        TIMEOUT="$2"
        shift 2
        ;;
        --)
        shift
        CMD="$@"
        break
        ;;
        *)
        usage
        ;;
    esac
done

if [ "$HOST" = "" ] || [ "$PORT" = "" ]; then
    echoerr "Error: you need to provide a host and port to test."
    usage
fi

wait_for

RESULT=$?

if [ "$CMD" != "" ] ; then
    if [ $RESULT -ne 0 ]; then
        echoerr "Strict mode, refusing to execute subprocess"
        exit $RESULT
    fi
    exec $CMD
else
    exit $RESULT
fi
