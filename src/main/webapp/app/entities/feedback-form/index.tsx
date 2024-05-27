import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import FeedbackForm from './feedback-form';
import FeedbackFormDetail from './feedback-form-detail';
import FeedbackFormUpdate from './feedback-form-update';
import FeedbackFormDeleteDialog from './feedback-form-delete-dialog';

const FeedbackFormRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<FeedbackForm />} />
    <Route path="new" element={<FeedbackFormUpdate />} />
    <Route path=":id">
      <Route index element={<FeedbackFormDetail />} />
      <Route path="edit" element={<FeedbackFormUpdate />} />
      <Route path="delete" element={<FeedbackFormDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default FeedbackFormRoutes;
