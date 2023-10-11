find -name \*.json | while read -r f; do
{
    result=`jq . "$f" 2>&1 1>/dev/null`
    if [ "${#result}" != 0 ]; then
        echo "$f: $result"
    fi
} &
done

: << 'EOM'
for wpid in `jobs -p`; do
	wait $wpid
done
EOM

wait -f
