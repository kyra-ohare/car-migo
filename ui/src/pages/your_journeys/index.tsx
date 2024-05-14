import { useEffect, useState } from 'react';
import { IJourneyEntity } from '../../interfaces';
import {
  useGetDriverJourneys,
  useGetPassengerJourneys,
} from '../../hooks/useJourney';
import { FloatingText, Journey } from '../../components';

export default function YourJourneys() {
  const [passengerJourneys, setPassengerJourneys] =
    useState<IJourneyEntity[]>();
  const [driverJourneys, setDriverJourneys] = useState<IJourneyEntity[]>();
  const { isSuccess: isPassengerJourneysSuccess, data: dataPassengerJourneys } =
    useGetPassengerJourneys();
  const { isSuccess: isDriverJourneysSuccess, data: dataDriverJourneys } =
    useGetDriverJourneys();

  useEffect(() => {
    if (isDriverJourneysSuccess && dataDriverJourneys) {
      setDriverJourneys(dataDriverJourneys);
    }
  }, [isDriverJourneysSuccess, dataDriverJourneys]);

  useEffect(() => {
    if (isPassengerJourneysSuccess && dataPassengerJourneys) {
      setPassengerJourneys(dataPassengerJourneys);
    }
  }, [isPassengerJourneysSuccess, dataPassengerJourneys]);

  return (
    <>
      {passengerJourneys && passengerJourneys[0] ? (
        <Journey
          label='Dear passenger, here are your upcoming journeys'
          journeys={passengerJourneys}
          datatestid='passenger-journeys'
        />
      ) : (
        <FloatingText text='Dear Passenger! You have not booked any journeys yet.' />
      )}
      {driverJourneys && driverJourneys[0] ? (
        <Journey
          label="Dear driver, here are the journeys you've created"
          journeys={driverJourneys}
          datatestid='driver-journeys'
        />
      ) : (
        <FloatingText text="Dear Driver! Don't forget to create journeys." />
      )}
    </>
  );
}
