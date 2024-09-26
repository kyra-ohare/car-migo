import { useNavigate } from 'react-router-dom';
import { Box, Button, Grid2 } from '@mui/material';
import { Info } from '@mui/icons-material';
import {
  CatchyMessage,
  TopRightButtonsContainer,
  UpperHalfContainer,
  WelcomeContainer,
  WelcomeMessage,
  WelcomeMessageContainer,
} from './styled';
import { ActionAreaCard, Footer, Search } from '../../components';
import Car from '../../assets/car.png';
import { useAuthStore } from '../../hooks/useAuthStore';
import { navigation } from '../../constants';

export default function Homepage() {
  const { isAuthorized } = useAuthStore();
  const navigate = useNavigate();

  const goToSignUp = () => {
    navigate(navigation.SIGN_UP_PAGE);
  };

  const goToSignIn = () => {
    navigate(navigation.SIGN_IN_PAGE);
  };

  return (
    <>
      <UpperHalfContainer>
        <WelcomeContainer>
          <WelcomeMessageContainer data-testid='welcome-message-container'>
            <WelcomeMessage data-testid='welcome-message'>
              Welcome to Car-Migo
            </WelcomeMessage>
          </WelcomeMessageContainer>
          {isAuthorized === false && (
            <>
              <TopRightButtonsContainer>
                <Button
                  variant='contained'
                  onClick={goToSignUp}
                  data-testid='sign-up-render'
                >
                  Sign up
                </Button>
                <Button
                  variant='contained'
                  onClick={goToSignIn}
                  data-testid='sign-in-render'
                >
                  Sign in
                </Button>
              </TopRightButtonsContainer>
            </>
          )}
        </WelcomeContainer>
        <Grid2 container>
           <Grid2
            size={{ xs: 12, sm: 6, md: 8 }}
            sx={{
              display: 'flex',
              justifyContent: 'center',
              alignItems: 'center',
            }}
          >
            <CatchyMessage data-testid='catchy-message'>
              The best way to get to places fast, at low cost and, most
              importantly, eco-friendly.
            </CatchyMessage>
          </Grid2>
          <Grid2 size={{ xs: 12, sm: 6, md: 4 }}>
            <Box
              component='img'
              sx={{
                display: 'inline-block',
                float: 'inline-end',
                height: 240,
                width: 400,
                maxHeight: { xs: 240, md: 240 },
                maxWidth: { xs: 400, md: 400 },
                margin: '10px',
                verticalAlign: 'middle',
              }}
              alt='car-migo'
              src={Car}
              data-testid='car-migo-pic'
            />
          </Grid2>
        </Grid2>
        <Search />
      </UpperHalfContainer>
      <ActionAreaCard
        id='what-is-it-card'
        label='what-is-it-card'
        icon={<Info style={{ width: 60, height: 60 }} />}
        imageAlt='Information'
        title='What is it?'
        text={
          <>
            It is a match-making system for drivers and passengers. You can
            either advertise rides or query them.
            <br />
            Drivers can create rides (a JourneyCard and a timeframe) while
            passengers can book them.
            <br />
            When inside the car share, you can enjoy the ride and make new
            friends.
            <br />
            Come onboard and experience this new lifestyle.
          </>
        }
        datatestid='what-is-it-card'
      />
      <ActionAreaCard
        id='why-card'
        label='why-card'
        icon={<Info style={{ width: 60, height: 60 }} />}
        imageAlt='Information'
        title='Why?'
        text={
          <>
            It is great for the environment once there will be less CO
            <sub>2</sub> released into the atmosphere.
            <br />
            Moreoever, there will be less traffic in our cities thus emergency
            vehicles will repond to emergencies more rapidly, less noise
            pollution, less road accidents, and you can make new friends to top
            it off.
            <br />
            The application is not about profiting but about car sharing so the
            passengers can pay the driver a fair amount for fuel costs.
          </>
        }
        datatestid='why-card'
      />
      <Footer />
    </>
  );
}
