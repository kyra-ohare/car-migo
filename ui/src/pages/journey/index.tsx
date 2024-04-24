import { useEffect, useState } from 'react';
import { IJourneyEntity } from '../../interfaces';
import {
  useGetDriverJourneys,
  useGetPassengerJourneys,
} from '../../hooks/useJourney';
import { FloatingText, JourneyV2 } from '../../components';

export default function Journey() {
  const [passengerJourneys, setPassengerJourneys] =
    useState<IJourneyEntity[]>();
  const [driverJourneys, setDriverJourneys] = useState<IJourneyEntity[]>();
  const [showResults, setShowResults] = useState<boolean>(true);
  const { isSuccess: isPassengerJourneysSuccess, data: dataPassengerJourneys } =
    useGetPassengerJourneys();
  const { isSuccess: isDriverJourneysSuccess, data: dataDriverJourneys } =
    useGetDriverJourneys();

  const resultState = (state: boolean) => {
    setShowResults(state);
    // setJourneys(undefined);
  };

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
        <JourneyV2
          label='As a passenger, here are your upcoming journeys'
          journeys={passengerJourneys}
        />
      ) : (
        <FloatingText text='Passenger! Click here to book some journeys.' />
      )}
      {driverJourneys && driverJourneys[0] ? (
        <JourneyV2
          label="As a driver, here are the journeys you've created"
          journeys={driverJourneys}
        />
      ) : (
        <FloatingText text='Driver! Click here to create some journeys.' />
      )}
    </>
  );
}
