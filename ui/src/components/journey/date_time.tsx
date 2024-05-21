import { Typography } from '@mui/material';
import { CalendarMonthRounded, AccessAlarmRounded } from '@mui/icons-material';
import { IJourneyResponseProps } from '../../interfaces';

export default function DateTime(props: IJourneyResponseProps) {
  const { date, time } = parseDateTime(props.journey.dateTime);

  return (
    <>
      <Typography variant='body1' sx={{ mb: 1 }}>
        <CalendarMonthRounded sx={{ verticalAlign: 'bottom', mr: 1 }} />
        {date}
      </Typography>
      <Typography variant='body1' sx={{ mb: 1 }}>
        <AccessAlarmRounded sx={{ verticalAlign: 'bottom', mr: 1 }} />
        {time}
      </Typography>
    </>
  );
}

function parseDateTime(dateTime: string) {
  const dateObj = new Date(dateTime);

  const date = [
    dateObj.getDate().toString().padStart(2, '0'),
    (dateObj.getMonth() + 1).toString().padStart(2, '0'),
    dateObj.getFullYear(),
  ].join('-');

  const time = [
    dateObj.getHours().toString().padStart(2, '0'),
    dateObj.getMinutes().toString().padStart(2, '0'),
  ].join(':');

  return {
    date,
    time,
  };
}
