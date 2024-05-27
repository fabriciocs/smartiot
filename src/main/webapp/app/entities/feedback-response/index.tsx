import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import FeedbackResponse from './feedback-response';
import FeedbackResponseDetail from './feedback-response-detail';
import FeedbackResponseUpdate from './feedback-response-update';
import FeedbackResponseDeleteDialog from './feedback-response-delete-dialog';

const FeedbackResponseRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<FeedbackResponse />} />
    <Route path="new" element={<FeedbackResponseUpdate />} />
    <Route path=":id">
      <Route index element={<FeedbackResponseDetail />} />
      <Route path="edit" element={<FeedbackResponseUpdate />} />
      <Route path="delete" element={<FeedbackResponseDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default FeedbackResponseRoutes;
