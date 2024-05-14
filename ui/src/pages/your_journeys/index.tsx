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
          label='As a passenger, here are your upcoming journeys'
          journeys={passengerJourneys}
          datatestid='passenger-journeys'
        />
      ) : (
        <FloatingText text='Passenger! Click here to book some journeys.' />
      )}
      {driverJourneys && driverJourneys[0] ? (
        <Journey
          label="As a driver, here are the journeys you've created"
          journeys={driverJourneys}
          datatestid='driver-journeys'
        />
      ) : (
        <FloatingText text='Driver! Click here to create some journeys.' />
      )}
    </>
  );
}
